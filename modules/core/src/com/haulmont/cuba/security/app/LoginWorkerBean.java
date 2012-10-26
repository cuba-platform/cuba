/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.security.app;

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.app.ServerConfig;
import com.haulmont.cuba.core.entity.HashMethod;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.NoUserSessionException;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.sys.UserSessionManager;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * Class that encapsulates the middleware login/logout functionality.
 *
 * @see com.haulmont.cuba.security.app.LoginServiceBean
 *
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean(LoginWorker.NAME)
public class LoginWorkerBean implements LoginWorker {
    private Log log = LogFactory.getLog(LoginWorkerBean.class);

    @Inject
    private Persistence persistence;

    @Inject
    private Messages messages;

    @Inject
    private Configuration configuration;

    @Inject
    private Encryption encryption;

    @Inject
    private UserSessionManager userSessionManager;

    @Inject
    private UserSessionSource userSessionSource;

    private User loadUser(String login, String password, Locale locale)
            throws LoginException {
        if (login == null)
            throw new IllegalArgumentException("Login is null");

        EntityManager em = persistence.getEntityManager();
        String queryStr = "select u from sec$User u where u.loginLowerCase = ?1 and (u.active = true or u.active is null)";

        Query q = em.createQuery(queryStr);
        q.setParameter(1, login.toLowerCase());

        List list = q.getResultList();
        if (list.isEmpty()) {
            log.warn("Failed to authenticate: " + login);
            if (password != null)
                throw new LoginException(getInvalidCredentialsMessage(login, locale));
            else
                throw new LoginException(
                        String.format(messages.getMessage(getClass(), "LoginException.InvalidActiveDirectoryUser", locale),
                                login));
        } else {
            User user = (User) list.get(0);
            return user;
        }
    }

    @Override
    public UserSession login(String login, String password, Locale locale) throws LoginException {
        if (password == null) {
            throw new LoginException(getInvalidCredentialsMessage(login, locale));
        }
        
        Transaction tx = persistence.createTransaction();
        try {
            User user = loadUser(login, password, locale);

            if (!encryption.checkUserAccess(user, password))
                throw new LoginException(getInvalidCredentialsMessage(login, locale));

            if (user.getLanguage() != null &&
                    BooleanUtils.isFalse(configuration.getConfig(GlobalConfig.class).getLocaleSelectVisible())) {
                locale = new Locale(user.getLanguage());
            }

            UserSession session = userSessionManager.createSession(user, locale, false);
            if (user.getDefaultSubstitutedUser() != null) {
                session = userSessionManager.createSession(session, user.getDefaultSubstitutedUser());
            }
            log.info("Logged in: " + session);

            tx.commit();

            userSessionManager.storeSession(session);

            return session;
        } finally {
            tx.end();
        }
    }

    private String getInvalidCredentialsMessage(String login, Locale locale) {
        String message = messages.getMessage(getClass(), "LoginException.InvalidLoginOrPassword", locale);
        return String.format(message, login);
    }

    @Override
    public UserSession loginSystem(String login, String password) throws LoginException {
        if (password == null) {
            throw new LoginException(getInvalidCredentialsMessage(login, Locale.getDefault()));
        }

        Transaction tx = persistence.createTransaction();
        try {
            User user = loadUser(login, password, Locale.getDefault());
            if (!encryption.checkUserAccess(user, password))
                throw new LoginException(getInvalidCredentialsMessage(login, Locale.getDefault()));

            UserSession session = userSessionManager.createSession(user, Locale.getDefault(), true);
            if (user.getDefaultSubstitutedUser() != null) {
                session = userSessionManager.createSession(session, user.getDefaultSubstitutedUser());
            }
            log.info("Logged in: " + session);

            tx.commit();

            userSessionManager.storeSession(session);

            return session;
        } finally {
            tx.end();
        }
    }

    @Override
    public UserSession loginTrusted(String login, String password, Locale locale) throws LoginException {
        String trustedClientPassword = configuration.getConfig(ServerConfig.class).getTrustedClientPassword();
        if (StringUtils.isBlank(trustedClientPassword) || !trustedClientPassword.equals(password))
            throw new LoginException(getInvalidCredentialsMessage(login, locale));

        Transaction tx = persistence.createTransaction();
        try {
            User user = loadUser(login, null, locale);
            Locale userLocale = locale;
            if (!StringUtils.isBlank(user.getLanguage())) {
                userLocale = new Locale(user.getLanguage());
            }
            UserSession session = userSessionManager.createSession(user, userLocale, false);
            if (user.getDefaultSubstitutedUser() != null) {
                session = userSessionManager.createSession(session, user.getDefaultSubstitutedUser());
            }
            log.info("Logged in: " + session);

            tx.commit();

            userSessionManager.storeSession(session);

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
            log.info("Logged out: " + session);
        } catch (SecurityException e) {
            log.warn("Couldn't logout: " + e);
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
            User user = em.find(User.class, substitutedUser.getId());
            if (user == null)
                throw new javax.persistence.NoResultException("User not found");

            UserSession session = userSessionManager.createSession(currentSession, user);

            tx.commit();

            userSessionManager.removeSession(currentSession);
            userSessionManager.storeSession(session);

            return session;
        } finally {
            tx.end();
        }
    }

    @Override
    public void ping() {
    }

    @Override
    public UserSession getSession(UUID sessionId) {
        try {
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
    public HashMethod getPasswordEncryptionMethod() {
        return configuration.getConfig(ServerConfig.class).getPasswordEncryption();
    }
}
