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

import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.NoUserSessionException;
import com.haulmont.cuba.security.entity.Profile;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.resource.Messages;
import com.haulmont.cuba.security.sys.UserSessionManager;
import com.haulmont.cuba.core.PersistenceProvider;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.SecurityProvider;

import javax.ejb.Stateless;
import java.util.List;
import java.util.Locale;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@Stateless(name = LoginWorker.JNDI_NAME)
public class LoginWorkerBean implements LoginWorker
{
    private Log log = LogFactory.getLog(LoginWorkerBean.class);

    private User loadUser(String login, String password, Locale locale)
            throws LoginException
    {
        EntityManager em = PersistenceProvider.getEntityManager();
        Query q = em.createQuery(
                "select u " +
                " from sec$User u join fetch u.profiles" +
                " where u.login = ?1 and u.password = ?2");
        q.setParameter(1, login);
        q.setParameter(2, password);
        List list = q.getResultList();
        if (list.isEmpty()) {
            log.warn("Failed to authenticate: " + login);
            throw new LoginException(
                    String.format(Messages.getString("LoginException.InvalidLoginOrPassword", locale), login));
        }
        else {
            User user = (User) list.get(0);
            return user;
        }
    }

    private User loadUser(String activeDirectoryUser, Locale locale)
            throws LoginException
    {
        EntityManager em = PersistenceProvider.getEntityManager();
        Query q = em.createQuery(
                "select u " +
                " from sec$User u join fetch u.profiles" +
                " where u.activeDirectoryUser = ?1");
        q.setParameter(1, activeDirectoryUser);
        List list = q.getResultList();
        if (list.isEmpty()) {
            log.warn("Failed to authenticate: " + activeDirectoryUser);
            throw new LoginException(
                    String.format(Messages.getString("LoginException.InvalidActiveDirectoryUser", locale), activeDirectoryUser));
        }
        else {
            User user = (User) list.get(0);
            return user;
        }
    }

    public UserSession login(String login, String password, Locale locale) throws LoginException {
        return login(login, password, null, locale);
    }

    public UserSession login(String login, String password, String profileName, Locale locale)
            throws LoginException
    {
        User user = loadUser(login, password, locale);
        UserSession session = findProfile(user, profileName, locale);
        log.info("Logged in: " + session);
        return session;
    }

    public UserSession loginActiveDirectory(String activeDirectoryUser, Locale locale) throws LoginException {
        return loginActiveDirectory(activeDirectoryUser, null, locale);
    }

    public UserSession loginActiveDirectory(String activeDirectoryUser, String profileName, Locale locale) throws LoginException {
        User user = loadUser(activeDirectoryUser, locale);
        UserSession session = findProfile(user, profileName, locale);
        log.info("Logged in: " + session);
        return session;
    }

    private UserSession findProfile(User user, String profileName, Locale locale) throws LoginException {
        Profile profile = null;
        if (profileName == null) {
            Iterator<Profile> it = user.getProfiles().iterator();
            while ((profile == null || !profile.isDefaultProfile()) && it.hasNext()) {
                profile = it.next();
            }
        }
        else {
            for (Profile p : user.getProfiles()) {
                if (profileName.equals(p.getName())) {
                    profile = p;
                    break;
                }
            }
        }
        if (profile == null)
           throw new LoginException(Messages.getString("LoginException.InvalidProfile", locale), profileName);

        return UserSessionManager.getInstance().createSession(user, profile, locale);
    }

    public void logout() {
        try {
            UserSession session = SecurityProvider.currentUserSession();
            UserSessionManager.getInstance().removeSession(session);
            log.info("Logged out: " + session);
        } catch (NoUserSessionException e) {
            log.warn("NoUserSessionException thrown on logout");
        }
    }

    public void ping() {
    }
}
