/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 26.11.2008 14:06:47
 *
 * $Id$
 */
package com.haulmont.cuba.security.app;

import com.haulmont.cuba.core.*;
import com.haulmont.cuba.core.app.ServerConfig;
import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.NoUserSessionException;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.sys.UserSessionManager;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * Worker bean providing middleware login/logout functionality.
 * Used by {@link com.haulmont.cuba.security.app.LoginServiceBean} and MBeans
 */
@ManagedBean(LoginWorker.NAME)
public class LoginWorkerBean implements LoginWorker
{
    private Log log = LogFactory.getLog(LoginWorkerBean.class);

    @Inject
    private UserSessionManager userSessionManager;

    private User loadUser(String login, String password, Locale locale)
            throws LoginException
    {
        if (login == null)
            throw new IllegalArgumentException("Login is null");

        EntityManager em = PersistenceProvider.getEntityManager();
        String queryStr = "select u from sec$User u where u.loginLowerCase = ?1 and (u.active = true or u.active is null)";
        if (password != null)
            queryStr += " and u.password = ?2";

        Query q = em.createQuery(queryStr);
        q.setParameter(1, login.toLowerCase());
        if (password != null)
            q.setParameter(2, password);

        List list = q.getResultList();
        if (list.isEmpty()) {
            log.warn("Failed to authenticate: " + login);
            if (password != null)
                throw new LoginException(
                        String.format(MessageProvider.getMessage(getClass(), "LoginException.InvalidLoginOrPassword", locale),
                                login));
            else
                throw new LoginException(
                        String.format(MessageProvider.getMessage(getClass(), "LoginException.InvalidActiveDirectoryUser", locale),
                                login));
        }
        else {
            User user = (User) list.get(0);
            return user;
        }
    }

    public UserSession login(String login, String password, Locale locale)
            throws LoginException
    {
        Transaction tx = Locator.createTransaction();
        try {
            User user = loadUser(login, password, locale);
            UserSession session = userSessionManager.createSession(user, locale, false);
            if (user.getDefaultSubstitutedUser() != null) {
                userSessionManager.updateSession(session, user.getDefaultSubstitutedUser());
            }
            log.info("Logged in: " + session);

            tx.commit();
            return session;
        } finally {
            tx.end();
        }
    }

    public UserSession loginSystem(String login, String password) throws LoginException {
        Transaction tx = Locator.createTransaction();
        try {
            User user = loadUser(login, password, Locale.getDefault());
            UserSession session = userSessionManager.createSession(user, Locale.getDefault(), true);
            if (user.getDefaultSubstitutedUser() != null) {
                userSessionManager.updateSession(session, user.getDefaultSubstitutedUser());
            }
            log.info("Logged in: " + session);

            tx.commit();
            return session;
        } finally {
            tx.end();
        }
    }

    public UserSession loginTrusted(String login, String password, Locale locale) throws LoginException {
        String trustedClientPassword = ConfigProvider.getConfig(GlobalConfig.class).getTrustedClientPassword();
        if (StringUtils.isBlank(trustedClientPassword) || !trustedClientPassword.equals(password))
            throw new LoginException(
                    String.format(MessageProvider.getMessage(getClass(), "LoginException.InvalidLoginOrPassword", locale), login)
            );

        Transaction tx = Locator.createTransaction();
        try {
            User user = loadUser(login, null, locale);
            UserSession session = userSessionManager.createSession(user, locale, false);
            if (user.getDefaultSubstitutedUser() != null) {
                userSessionManager.updateSession(session, user.getDefaultSubstitutedUser());
            }
            log.info("Logged in: " + session);

            tx.commit();
            return session;
        } finally {
            tx.end();
        }
    }

    public void logout() {
        try {
            UserSession session = SecurityProvider.currentUserSession();
            userSessionManager.removeSession(session);
            log.info("Logged out: " + session);
        }
        catch (SecurityException e) {
            log.warn("Couldn't logout: " + e);
        }
        catch (NoUserSessionException e) {
            log.warn("NoUserSessionException thrown on logout");
        }
    }

    public UserSession substituteUser(User substitutedUser) {
        UserSession currentSession = SecurityProvider.currentUserSession();

        Transaction tx = Locator.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();
            User user = em.find(User.class, substitutedUser.getId());
            if (user == null)
                throw new javax.persistence.NoResultException("User not found");

            UserSession session = userSessionManager.updateSession(currentSession, user);

            tx.commit();

            return session;
        } finally {
            tx.end();
        }
    }

    public void ping() {
    }

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
}
