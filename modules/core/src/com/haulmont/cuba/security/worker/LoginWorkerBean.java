/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 26.11.2008 14:06:47
 *
 * $Id$
 */
package com.haulmont.cuba.security.worker;

import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.NoUserSessionException;
import com.haulmont.cuba.security.entity.Profile;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.resource.Messages;
import com.haulmont.cuba.security.impl.UserSessionManager;
import com.haulmont.cuba.core.PersistenceProvider;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.SecurityProvider;

import javax.ejb.Stateless;
import java.util.List;
import java.util.Locale;
import java.util.ArrayList;

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
                " where u.login = ?1 and u.password = ?2 and u.deleteTs is null");
        q.setParameter(1, login);
        q.setParameter(2, password);
        List list = q.getResultList();
        if (list.isEmpty()) {
            log.warn("Failed to authenticate: " + login);
            throw new LoginException(Messages.getString("LoginException.InvalidLoginOrPassword", locale));
        }
        else {
            User user = (User) list.get(0);
            return user;
        }
    }

    public List<Profile> authenticate(String login, String password, Locale locale)
            throws LoginException
    {
        log.info("Authenticating: " + login);
        User user = loadUser(login, password, locale);

        List<Profile> list = new ArrayList<Profile>(user.getProfiles());
        return list;
    }

    public UserSession login(String login, String password, String profileName, Locale locale)
            throws LoginException
    {
        if (profileName == null)
            throw new IllegalArgumentException("profileName can not be null");

        User user = loadUser(login, password, locale);
        Profile profile = null;
        for (Profile p : user.getProfiles()) {
            if (profileName.equals(p.getName()) && !p.isDeleted()) {
                profile = p;
                break;
            }
        }
         if (profile == null)
            throw new LoginException(Messages.getString("LoginException.InvalidProfile", locale), profileName);

        UserSession session = UserSessionManager.getInstance().createSession(user, profile, locale);
        log.info("Logged in: " + session);
        return session;
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
