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
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.security.app.UserSessionsAPI;
import com.haulmont.cuba.security.auth.events.*;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.*;
import com.haulmont.cuba.security.sys.UserSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import java.io.Serializable;
import java.util.*;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;
import static com.haulmont.cuba.core.sys.AppContext.withSecurityContext;

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
    protected UserSessionsAPI userSessions;
    @Inject
    protected Persistence persistence;
    @Inject
    protected ClusterManager clusterManager;

    @Inject
    protected List<AuthenticationProvider> authenticationProviders;

    protected UserSession serverSession;

    public AuthenticationManagerBean() {
        //noinspection IncorrectCreateEntity
        User noUser = new User();
        noUser.setLogin("server");
        serverSession = new UserSession(
                UUID.fromString("a66abe96-3b9d-11e2-9db2-3860770d7eaf"), noUser,
                Collections.emptyList(), Locale.ENGLISH, true) {
            @Override
            public UUID getId() {
                return AppContext.NO_USER_CONTEXT.getSessionId();
            }
        };
    }

    @Override
    @Nonnull
    public AuthenticationDetails authenticate(Credentials credentials) throws LoginException {
        checkNotNullArgument(credentials, "credentials should not be null");

        SecurityContext previousSecurityContext = AppContext.getSecurityContext();
        AppContext.setSecurityContext(new SecurityContext(serverSession));

        try (Transaction tx = persistence.createTransaction()) {
            AuthenticationDetails authenticationDetails = authenticateInternal(credentials);

            tx.commit();

            userSessionManager.clearPermissionsOnUser(authenticationDetails.getSession());

            return authenticationDetails;
        } finally {
            AppContext.setSecurityContext(previousSecurityContext);
        }
    }

    @Override
    @Nonnull
    public AuthenticationDetails login(Credentials credentials) throws LoginException {
        checkNotNullArgument(credentials, "credentials should not be null");

        SecurityContext previousSecurityContext = AppContext.getSecurityContext();
        AppContext.setSecurityContext(new SecurityContext(serverSession));

        AuthenticationDetails authenticationDetails = null;
        try {
            try (Transaction tx = persistence.createTransaction()) {
                publishBeforeLoginEvent(credentials);

                authenticationDetails = authenticateInternal(credentials);

                tx.commit();

                userSessionManager.clearPermissionsOnUser(authenticationDetails.getSession());

                setTimeZone(credentials, authenticationDetails);

                setSessionAttributes(credentials, authenticationDetails);

                storeSession(credentials, authenticationDetails);

                log.info("Logged in: {}", authenticationDetails.getSession());

                publishUserLoggedInEvent(credentials, authenticationDetails);

                return authenticationDetails;
            } finally {
                publishAfterLoginEvent(credentials, authenticationDetails);
            }
        } finally {
            AppContext.setSecurityContext(previousSecurityContext);
        }
    }

    protected void setTimeZone(Credentials credentials, AuthenticationDetails authenticationDetails) {
        if (credentials instanceof TimeZoneProvider) {
            TimeZone timeZone = ((TimeZoneProvider) credentials).getTimeZone();

            UserSession session = authenticationDetails.getSession();

            if (Boolean.TRUE.equals(session.getUser().getTimeZoneAuto())) {
                session.setTimeZone(timeZone);
            }
        }
    }

    protected void setSessionAttributes(Credentials credentials, AuthenticationDetails authenticationDetails) {
        if (credentials instanceof SessionAttributesProvider) {
            Map<String, Serializable> sessionAttributes =
                    ((SessionAttributesProvider) credentials).getSessionAttributes();
            if (sessionAttributes != null) {
                UserSession session = authenticationDetails.getSession();

                for (Map.Entry<String, Serializable> attribute : sessionAttributes.entrySet()) {
                    session.setAttribute(attribute.getKey(), attribute.getValue());
                }
            }
        }
    }

    protected void storeSession(Credentials credentials, AuthenticationDetails authenticationDetails) {
        if (credentials instanceof SyncSessionCredentials
                && ((SyncSessionCredentials) credentials).isSyncNewUserSessionReplication()) {
            boolean saved = clusterManager.getSyncSendingForCurrentThread();
            clusterManager.setSyncSendingForCurrentThread(true);
            try {
                userSessions.add(authenticationDetails.getSession());
            } finally {
                clusterManager.setSyncSendingForCurrentThread(saved);
            }
        } else {
            userSessions.add(authenticationDetails.getSession());
        }
    }

    @Nonnull
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

            withSecurityContext(new SecurityContext(serverSession), () ->
                    publishUserSubstitutedEvent(currentSession, session)
            );

            tx.commit();

            userSessions.remove(currentSession);
            userSessionManager.clearPermissionsOnUser(session);
            userSessions.add(session);

            return session;
        }
    }

    @Override
    public void logout() {
        try {
            UserSession session = userSessionSource.getUserSession();
            userSessions.remove(session);
            log.info("Logged out: {}", session);

            withSecurityContext(new SecurityContext(serverSession), () ->
                    publishUserLoggedOut(session)
            );
        } catch (SecurityException e) {
            log.warn("Couldn't logout: {}", e);
        } catch (NoUserSessionException e) {
            log.warn("NoUserSessionException thrown on logout");
        }
    }

    protected AuthenticationDetails authenticateInternal(Credentials credentials) throws LoginException {
        AuthenticationDetails details = null;

        Class<? extends Credentials> credentialsClass = credentials.getClass();

        try {
            publishBeforeAuthenticationEvent(credentials);

            List<AuthenticationProvider> providers = getProviders();

            for (AuthenticationProvider provider : providers) {
                if (!provider.supports(credentialsClass)) {
                    continue;
                }

                log.trace("Authentication attempt using {}", provider.getClass().getName());

                try {
                    details = provider.authenticate(credentials);

                    if (details != null) {
                        if (details.getSession() == null) {
                            throw new InternalAuthenticationException(
                                    "Authentication provider returned authentication details without session");
                        }

                        log.trace("Authentication successful for {}", credentials);

                        // publish auth success
                        publishAuthenticationSuccess(details, credentials);

                        return details;
                    }
                } catch (LoginException e) {
                    // publish auth fail
                    publishAuthenticationFailed(credentials, provider, e);

                    throw e;
                } catch (RuntimeException re) {
                    log.error("Exception is thrown by authentication provider", re);

                    InternalAuthenticationException ie =
                            new InternalAuthenticationException("Exception is thrown by authentication provider", re);

                    // publish auth fail
                    publishAuthenticationFailed(credentials, provider, ie);

                    throw ie;
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
                "select su from sec$User su where " +
                        "su in (select s.substitutedUser from sec$User u join u.substitutions s " +
                        "where u.id = ?1 and s.substitutedUser.id = ?2)",
                User.class
        );
        query.setParameter(1, currentSession.getUser().getId());
        query.setParameter(2, substitutedUser.getId());
        List<User> list = query.getResultList();
        if (list.isEmpty()) {
            throw new NoResultException("User not found");
        }

        return list.get(0);
    }

    protected void publishAfterLoginEvent(Credentials credentials, AuthenticationDetails authenticationDetails) {
        events.publish(new AfterLoginEvent(credentials, authenticationDetails));
    }

    protected void publishUserSubstitutedEvent(UserSession currentSession, UserSession substitutedSession) {
        events.publish(new UserSubstitutedEvent(currentSession, substitutedSession));
    }

    protected void publishUserLoggedInEvent(Credentials credentials, AuthenticationDetails authenticationDetails) {
        events.publish(new UserLoggedInEvent(credentials, authenticationDetails));
    }

    protected void publishBeforeLoginEvent(Credentials credentials) throws LoginException {
        events.publish(new BeforeLoginEvent(credentials));
    }

    protected void publishBeforeAuthenticationEvent(Credentials credentials) throws LoginException {
        events.publish(new BeforeAuthenticationEvent(credentials));
    }

    protected void publishAfterAuthenticationEvent(Credentials credentials, AuthenticationDetails authenticationDetails)
            throws LoginException {
        events.publish(new AfterAuthenticationEvent(credentials, authenticationDetails));
    }

    protected void publishAuthenticationFailed(Credentials credentials, AuthenticationProvider provider,
                                               LoginException e) throws LoginException {
        events.publish(new AuthenticationFailureEvent(credentials, provider, e));
    }

    protected void publishAuthenticationSuccess(AuthenticationDetails details, Credentials credentials) throws LoginException {
        events.publish(new AuthenticationSuccessEvent(credentials, details));
    }

    protected void publishUserLoggedOut(UserSession session) {
        events.publish(new UserLoggedOutEvent(session));
    }

    protected List<AuthenticationProvider> getProviders() {
        return authenticationProviders;
    }
}