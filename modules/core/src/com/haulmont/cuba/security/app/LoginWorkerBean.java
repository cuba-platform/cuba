/*
 * Copyright (c) 2008-2016 Haulmont.
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
 *
 */
package com.haulmont.cuba.security.app;

import com.haulmont.cuba.core.*;
import com.haulmont.cuba.core.app.ClusterManagerAPI;
import com.haulmont.cuba.core.app.ServerConfig;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.remoting.RemoteClientInfo;
import com.haulmont.cuba.security.entity.RememberMeToken;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.NoUserSessionException;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.sys.TrustedLoginHandler;
import com.haulmont.cuba.security.sys.UserSessionManager;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.*;

/**
 * Class that encapsulates the middleware login/logout functionality.
 *
 * @see com.haulmont.cuba.security.app.LoginServiceBean
 */
@Component(LoginWorker.NAME)
public class LoginWorkerBean implements LoginWorker, AppContext.Listener, Ordered {

    private Logger log = LoggerFactory.getLogger(LoginWorkerBean.class);

    protected static final String MSG_PACK = "com.haulmont.cuba.security";

    @Inject
    protected Persistence persistence;

    @Inject
    protected Messages messages;

    protected Configuration configuration;

    @Inject
    protected PasswordEncryption passwordEncryption;

    @Inject
    protected UserSessionManager userSessionManager;

    @Inject
    protected UserSessionSource userSessionSource;

    @Inject
    protected TrustedLoginHandler trustedLoginHandler;

    @Inject
    protected ClusterManagerAPI clusterManager;

    @Inject
    protected Authentication authentication;

    @Inject
    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    @Nullable
    protected User loadUser(String login) throws LoginException {
        if (login == null)
            throw new IllegalArgumentException("Login is null");

        EntityManager em = persistence.getEntityManager();
        String queryStr = "select u from sec$User u where u.loginLowerCase = ?1 and (u.active = true or u.active is null)";

        Query q = em.createQuery(queryStr);
        q.setParameter(1, login.toLowerCase());

        List list = q.getResultList();
        if (list.isEmpty()) {
            log.warn("Failed to authenticate: " + login);
            return null;
        } else {
            //noinspection UnnecessaryLocalVariable
            User user = (User) list.get(0);
            return user;
        }
    }

    @Nullable
    protected RememberMeToken loadRememberMeToken(String rememberMeToken, User user) {
        EntityManager em = persistence.getEntityManager();
        TypedQuery<RememberMeToken> query = em.createQuery(
                "select rt from sec$RememberMeToken rt where rt.token = :token and rt.user.id = :userId",
                RememberMeToken.class);
        query.setParameter("token", rememberMeToken);
        query.setParameter("userId", user.getId());

        return query.getFirstResult();
    }

    @Override
    public UserSession login(String login, String password, Locale locale) throws LoginException {
        return login(login, password, locale, Collections.emptyMap());
    }

    @Override
    public UserSession login(String login, String password, Locale locale, Map<String, Object> params) throws LoginException {
        if (password == null)
            throw new LoginException(getInvalidCredentialsMessage(login, locale));

        Transaction tx = persistence.createTransaction();
        try {
            User user = loadUser(login);
            if (user == null)
                throw new LoginException(getInvalidCredentialsMessage(login, locale));

            if (!passwordEncryption.checkPassword(user, password))
                throw new LoginException(getInvalidCredentialsMessage(login, locale));

            Locale userLocale = locale;
            if (user.getLanguage() != null &&
                    BooleanUtils.isFalse(configuration.getConfig(GlobalConfig.class).getLocaleSelectVisible())) {
                userLocale = new Locale(user.getLanguage());
            }

            UserSession session = userSessionManager.createSession(user, userLocale, false);
            checkPermissions(login, params, userLocale, session);

            tx.commit();

            userSessionManager.clearPermissionsOnUser(session);
            Boolean sync = (Boolean) params.get(ServerConfig.SYNC_NEW_USER_SESSION_REPLICATION_PROP);
            if (sync != null && sync) {
                boolean saved = clusterManager.getSyncSendingForCurrentThread();
                clusterManager.setSyncSendingForCurrentThread(true);
                try {
                    userSessionManager.storeSession(session);
                } finally {
                    clusterManager.setSyncSendingForCurrentThread(saved);
                }
            } else {
                userSessionManager.storeSession(session);
            }

            log.info("Logged in: {}", session);

            return session;
        } finally {
            tx.end();
        }
    }

    protected String getInvalidCredentialsMessage(String login, Locale locale) {
        return messages.formatMessage(MSG_PACK, "LoginException.InvalidLoginOrPassword", locale, login);
    }

    @Override
    public UserSession loginSystem(String login) throws LoginException {
        Locale locale = messages.getTools().trimLocale(messages.getTools().getDefaultLocale());

        Transaction tx = persistence.createTransaction();
        try {
            User user = loadUser(login);
            if (user == null) {
                throw new LoginException(getInvalidCredentialsMessage(login, locale));
            }

            UserSession session = userSessionManager.createSession(user, locale, true);

            tx.commit();

            userSessionManager.clearPermissionsOnUser(session);
            userSessionManager.storeSession(session);

            log.info("Logged in: {}", session);

            return session;
        } finally {
            tx.end();
        }
    }

    @Override
    public UserSession loginAnonymous() throws LoginException {
        GlobalConfig globalConfig = configuration.getConfig(GlobalConfig.class);
        UUID anonymousSessionId = globalConfig.getAnonymousSessionId();

        ServerConfig serverConfig = configuration.getConfig(ServerConfig.class);
        String anonymousLogin = serverConfig.getAnonymousLogin();

        Locale locale = messages.getTools().trimLocale(messages.getTools().getDefaultLocale());

        Transaction tx = persistence.createTransaction();
        try {
            User user = loadUser(anonymousLogin);
            if (user == null) {
                throw new LoginException(getInvalidCredentialsMessage(anonymousLogin, locale));
            }

            UserSession session = userSessionManager.createSession(anonymousSessionId, user, locale, true);
            session.setClientInfo("System anonymous session");
            tx.commit();

            userSessionManager.clearPermissionsOnUser(session);
            userSessionManager.storeSession(session);

            log.info("Logged in: {}", session);

            return session;
        } finally {
            tx.end();
        }
    }

    @Override
    public UserSession getSystemSession(String trustedClientPassword) throws LoginException {
        RemoteClientInfo remoteClientInfo = RemoteClientInfo.get();
        if (remoteClientInfo != null) {
            // reject request from not permitted client ip
            if (!trustedLoginHandler.checkAddress(remoteClientInfo.getAddress())) {
                log.warn("Attempt of trusted login from not permitted IP address: {}", remoteClientInfo.getAddress());
                throw new LoginException(getInvalidCredentialsMessage(remoteClientInfo.getAddress(),
                        messages.getTools().getDefaultLocale()));
            }
        }

        if (!trustedLoginHandler.checkPassword(trustedClientPassword)) {
            throw new LoginException(getInvalidCredentialsMessage(AppContext.getProperty("cuba.jmxUserLogin"),
                    messages.getTools().getDefaultLocale()));
        }

        UserSession userSession = authentication.begin();
        authentication.end();

        return userSession;
    }

    @Override
    public UserSession loginTrusted(String login, String password, Locale locale) throws LoginException {
        return loginTrusted(login, password, locale, Collections.emptyMap());
    }

    @Override
    public UserSession loginTrusted(String login, String password, Locale locale, Map<String, Object> params) throws LoginException {
        RemoteClientInfo remoteClientInfo = RemoteClientInfo.get();
        if (remoteClientInfo != null) {
            // reject request from not permitted client ip
            if (!trustedLoginHandler.checkAddress(remoteClientInfo.getAddress())) {
                log.warn("Attempt of trusted login from not permitted IP address: {} {}", login, remoteClientInfo.getAddress());
                throw new LoginException(getInvalidCredentialsMessage(login, locale));
            }
        } else {
            log.debug("Unable to check trusted client IP when obtaining system session");
        }

        if (!trustedLoginHandler.checkPassword(password)) {
            throw new LoginException(getInvalidCredentialsMessage(login, locale));
        }

        Transaction tx = persistence.createTransaction();
        try {
            User user = loadUser(login);

            if (user == null) {
                throw new LoginException(messages.formatMessage(MSG_PACK, "LoginException.InvalidUser", locale, login));
            }

            Locale userLocale = locale;
            if (user.getLanguage() != null &&
                    BooleanUtils.isFalse(configuration.getConfig(GlobalConfig.class).getLocaleSelectVisible())) {
                userLocale = new Locale(user.getLanguage());
            }
            UserSession session = userSessionManager.createSession(user, userLocale, false);
            checkPermissions(login, params, userLocale, session);

            log.info("Logged in: {}", session);

            tx.commit();

            userSessionManager.clearPermissionsOnUser(session);
            userSessionManager.storeSession(session);

            return session;
        } finally {
            tx.end();
        }
    }

    @Override
    public UserSession loginByRememberMe(String login, String rememberMeToken, Locale locale) throws LoginException {
        return loginByRememberMe(login, rememberMeToken, locale, Collections.emptyMap());
    }

    @Override
    public UserSession loginByRememberMe(String login, String rememberMeToken, Locale locale, Map<String, Object> params)
            throws LoginException {
        Transaction tx = persistence.createTransaction();
        try {
            User user = loadUser(login);

            if (user == null) {
                throw new LoginException(messages.formatMessage(MSG_PACK, "LoginException.InvalidUser", locale, login));
            }

            RememberMeToken loginToken = loadRememberMeToken(rememberMeToken, user);
            if (loginToken == null) {
                throw new LoginException(getInvalidCredentialsMessage(login, locale));
            }

            Locale userLocale = locale;
            if (user.getLanguage() != null &&
                    BooleanUtils.isFalse(configuration.getConfig(GlobalConfig.class).getLocaleSelectVisible())) {
                userLocale = new Locale(user.getLanguage());
            }
            UserSession session = userSessionManager.createSession(user, userLocale, false);
            checkPermissions(login, params, userLocale, session);

            tx.commit();

            userSessionManager.clearPermissionsOnUser(session);
            userSessionManager.storeSession(session);

            log.info("Logged in: {}", session);

            return session;
        } finally {
            tx.end();
        }
    }

    @Override
    public void logout() {
        try {
            UserSession session = userSessionSource.getUserSession();
            userSessionManager.removeSession(session);
            log.info("Logged out: {}", session);
        } catch (SecurityException e) {
            log.warn("Couldn't logout: {}", e);
        } catch (NoUserSessionException e) {
            log.warn("NoUserSessionException thrown on logout");
        }
    }

    @Override
    public UserSession substituteUser(User substitutedUser) {
        UserSession currentSession = userSessionSource.getUserSession();

        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();

            User user;
            if (currentSession.getUser().equals(substitutedUser)) {
                user = em.find(User.class, substitutedUser.getId());
                if (user == null)
                    throw new javax.persistence.NoResultException("User not found");
            } else {
                TypedQuery<User> query = em.createQuery(
                        "select s.substitutedUser from sec$User u join u.substitutions s " +
                        "where u.id = ?1 and s.substitutedUser.id = ?2",
                        User.class
                );
                query.setParameter(1, currentSession.getUser());
                query.setParameter(2, substitutedUser);
                List<User> list = query.getResultList();
                if (list.isEmpty())
                    throw new javax.persistence.NoResultException("User not found");
                else
                    user = list.get(0);
            }

            UserSession session = userSessionManager.createSession(currentSession, user);

            tx.commit();

            userSessionManager.removeSession(currentSession);
            userSessionManager.clearPermissionsOnUser(session);
            userSessionManager.storeSession(session);

            return session;
        } finally {
            tx.end();
        }
    }

    @Override
    public UserSession getSession(UUID sessionId) {
        try {
            //noinspection UnnecessaryLocalVariable
            UserSession session = userSessionManager.getSession(sessionId);
            return session;
        } catch (RuntimeException e) {
            if (e instanceof NoUserSessionException)
                return null;
            else
                throw e;
        }
    }

    @Override
    public boolean checkRememberMe(String login, String rememberMeToken) {
        boolean verified = false;

        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            TypedQuery<RememberMeToken> query = em.createQuery(
                    "select rt from sec$RememberMeToken rt where rt.token = :token and rt.user.loginLowerCase = :userLogin",
                    RememberMeToken.class);
            query.setParameter("token", rememberMeToken);
            query.setParameter("userLogin", StringUtils.lowerCase(login));

            if (query.getFirstResult() != null) {
                verified = true;
            }

            tx.commit();
        } finally {
            tx.end();
        }
        return verified;
    }

    protected void checkPermissions(String login, Map<String, Object> params, Locale userLocale, UserSession session)
            throws LoginException {
        String clientTypeParam = (String) params.get(ClientType.class.getName());
        if (ClientType.DESKTOP.name().equals(clientTypeParam) || ClientType.WEB.name().equals(clientTypeParam)) {
            if (!session.isSpecificPermitted("cuba.gui.loginToClient")) {
                log.warn("Attempt of login to {} for user '{}' without cuba.gui.loginToClient permission",
                        clientTypeParam, login);

                throw new LoginException(getInvalidCredentialsMessage(login, userLocale));
            }
        }
    }

    @PostConstruct
    public void init() {
        AppContext.addListener(this);
    }

    protected void initializeAnonymousSession() {
        log.debug("Initialize anonymous session");

        try {
            UserSession session = loginAnonymous();

            log.debug("Anonymous session initialized with id {}", session.getId());
        } catch (LoginException e) {
            log.error("Unable to login anonymous session", e);
        }
    }

    @Override
    public void applicationStarted() {
        initializeAnonymousSession();
    }

    @Override
    public void applicationStopped() {
    }

    @Override
    public int getOrder() {
        return LOWEST_PLATFORM_PRECEDENCE - 110;
    }
}