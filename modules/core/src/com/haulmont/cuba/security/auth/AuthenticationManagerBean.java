/*
 * Copyright (c) 2008-2017 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.haulmont.cuba.security.auth;

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.TypedQuery;
import com.haulmont.cuba.core.app.ClusterManager;
import com.haulmont.cuba.core.global.Events;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.security.auth.events.*;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.NoUserSessionException;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.sys.UserSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.List;

@Component(AuthenticationManager.NAME)
public class AuthenticationManagerBean implements AuthenticationManager {

    private final Logger log = LoggerFactory.getLogger(AuthenticationManagerBean.class);

    @Inject
    protected Events events;
    @Inject
    protected UserSessionSource userSessionSource;
    @Inject
    protected UserSessionManager userSessionManager;
    @Inject
    protected Persistence persistence;
    @Inject
    protected ClusterManager clusterManager;

    @Inject
    protected List<AuthenticationProvider> authenticationProviders;

    @Override
    @Nonnull
    public AuthenticationDetails login(Credentials credentials) throws LoginException {
        AuthenticationDetails authenticationDetails = null;

        try (Transaction tx = persistence.getTransaction()) {
            publishBeforeLoginEvent(credentials);

            authenticationDetails = authenticateInternal(credentials);

            tx.commit();

            userSessionManager.clearPermissionsOnUser(authenticationDetails.getSession());

            if (credentials instanceof SyncSessionCredentials
                    && ((SyncSessionCredentials) credentials).isSyncNewUserSessionReplication()) {
                boolean saved = clusterManager.getSyncSendingForCurrentThread();
                clusterManager.setSyncSendingForCurrentThread(true);
                try {
                    userSessionManager.storeSession(authenticationDetails.getSession());
                } finally {
                    clusterManager.setSyncSendingForCurrentThread(saved);
                }
            } else {
                userSessionManager.storeSession(authenticationDetails.getSession());
            }

            log.info("Logged in: {}", authenticationDetails.getSession());

            publishUserLoggedInEvent(authenticationDetails.getSession());

            return authenticationDetails;
        } finally {
            UserSession userSession = authenticationDetails != null ? authenticationDetails.getSession() : null;

            publishAfterLoginEvent(credentials, userSession);
        }
    }

    @Override
    @Nonnull
    public AuthenticationDetails authenticate(Credentials credentials) throws LoginException {
        try (Transaction tx = persistence.getTransaction()) {
            AuthenticationDetails authenticationDetails = authenticateInternal(credentials);

            tx.commit();

            userSessionManager.clearPermissionsOnUser(authenticationDetails.getSession());

            return authenticationDetails;
        }
    }

    @Override
    public void logout() {
        try {
            UserSession session = userSessionSource.getUserSession();
            userSessionManager.removeSession(session);
            log.info("Logged out: {}", session);

            publishUserLoggedOut(session);

        } catch (SecurityException e) {
            log.warn("Couldn't logout: {}", e);
        } catch (NoUserSessionException e) {
            log.warn("NoUserSessionException thrown on logout");
        }
    }

    @Override
    public UserSession substituteUser(User substitutedUser) {
        UserSession currentSession = userSessionSource.getUserSession();

        try (Transaction tx = persistence.createTransaction()) {
            EntityManager em = persistence.getEntityManager();

            User user;
            if (currentSession.getUser().equals(substitutedUser)) {
                user = em.find(User.class, substitutedUser.getId());
                if (user == null) {
                    throw new NoResultException("User not found");
                }
            } else {
                user = loadSubstitutedUser(substitutedUser, currentSession, em);
            }

            UserSession session = userSessionManager.createSession(currentSession, user);

            publishUserSubstitutedEvent(currentSession, session);

            tx.commit();

            userSessionManager.removeSession(currentSession);
            userSessionManager.clearPermissionsOnUser(session);
            userSessionManager.storeSession(session);

            return session;
        }
    }

    protected AuthenticationDetails authenticateInternal(Credentials credentials) throws LoginException {
        publishBeforeAuthenticationEvent(credentials);

        Class<? extends Credentials> credentialsClass = credentials.getClass();

        AuthenticationDetails details = null;
        try {
            for (AuthenticationProvider provider : getProviders()) {
                if (!provider.supports(credentialsClass)) {
                    continue;
                }

                log.debug("Authentication attempt using {}", provider.getClass().getName());

                try {
                    details = provider.authenticate(credentials);

                    if (details != null) {
                        log.debug("Authentication successful for {}", credentials);

                        // publish auth success
                        publishAuthenticationSuccess(details, credentials);

                        return details;
                    }
                } catch (LoginException e) {
                    // publish auth fail
                    publishAuthenticationFailed(credentials, provider, e);

                    throw e;
                }
            }
        } finally {
            publishAfterAuthenticationEvent(credentials, details);
        }

        throw new UnsupportedCredentialsException(
                "Unable to find authentication provider that supports credentials class "
                        + credentialsClass.getName());
    }

    protected User loadSubstitutedUser(User substitutedUser, UserSession currentSession, EntityManager em) {
        TypedQuery<User> query = em.createQuery(
                "select s.substitutedUser from sec$User u join u.substitutions s " +
                        "where u.id = ?1 and s.substitutedUser.id = ?2",
                User.class
        );
        query.setParameter(1, currentSession.getUser());
        query.setParameter(2, substitutedUser);
        List<User> list = query.getResultList();
        if (list.isEmpty()) {
            throw new NoResultException("User not found");
        }

        return list.get(0);
    }

    protected void publishAfterLoginEvent(Credentials credentials, UserSession userSession) {
        events.publish(new AfterLoginEvent(credentials, userSession));
    }

    protected void publishUserSubstitutedEvent(UserSession currentSession, UserSession substitutedSession) {
        events.publish(new UserSubstitutedEvent(currentSession, substitutedSession));
    }

    protected void publishUserLoggedInEvent(UserSession userSession) {
        events.publish(new UserLoggedInEvent(userSession));
    }

    protected void publishBeforeLoginEvent(Credentials credentials) throws LoginException {
        try {
            events.publish(new BeforeLoginEvent(credentials));
        } catch (UndeclaredThrowableException e) {
            rethrowLoginException(e);
        }
    }

    protected void publishBeforeAuthenticationEvent(Credentials credentials) throws LoginException {
        try {
            events.publish(new BeforeAuthenticationEvent(credentials));
        } catch (UndeclaredThrowableException e) {
            rethrowLoginException(e);
        }
    }

    protected void rethrowLoginException(RuntimeException e) throws LoginException {
        Throwable cause = e.getCause();
        if (cause instanceof LoginException) {
            throw (LoginException) cause;
        } else {
            throw e;
        }
    }

    protected void publishAfterAuthenticationEvent(Credentials credentials, AuthenticationDetails authenticationDetails)
            throws LoginException {
        try {
            events.publish(new AfterAuthenticationEvent(credentials, authenticationDetails));
        } catch (UndeclaredThrowableException e) {
            rethrowLoginException(e);
        }
    }

    protected void publishAuthenticationFailed(Credentials credentials, AuthenticationProvider provider,
                                               LoginException e) throws LoginException {
        try {
            events.publish(new AuthenticationFailureEvent(credentials, provider, e));
        } catch (UndeclaredThrowableException re) {
            rethrowLoginException(re);
        }
    }

    protected void publishAuthenticationSuccess(AuthenticationDetails details, Credentials credentials) throws LoginException {
        try {
            events.publish(new AuthenticationSuccessEvent(credentials, details));
        } catch (UndeclaredThrowableException re) {
            rethrowLoginException(re);
        }
    }

    protected void publishUserLoggedOut(UserSession session) {
        events.publish(new UserLoggedOutEvent(session));
    }

    public List<AuthenticationProvider> getProviders() {
        return authenticationProviders;
    }
}