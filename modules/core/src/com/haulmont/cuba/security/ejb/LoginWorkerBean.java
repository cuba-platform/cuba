/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 26.11.2008 14:06:47
 *
 * $Id$
 */
package com.haulmont.cuba.security.ejb;

import com.haulmont.cuba.security.intf.UserSession;
import com.haulmont.cuba.security.intf.LoginException;
import com.haulmont.cuba.security.entity.Profile;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.resources.Messages;
import com.haulmont.cuba.core.PersistenceProvider;
import com.haulmont.cuba.core.EntityManagerAdapter;
import com.haulmont.cuba.core.QueryAdapter;

import javax.ejb.Stateless;
import java.util.List;
import java.util.UUID;
import java.util.Locale;
import java.util.ArrayList;
import java.security.Principal;

import org.jboss.security.SecurityAssociation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@Stateless(name = LoginWorker.JNDI_NAME)
public class LoginWorkerBean implements LoginWorker
{
    private Log log = LogFactory.getLog(LoginWorkerBean.class);

    private List<Object[]> __authenticate(String login, String password, Locale locale)
            throws LoginException
    {
        EntityManagerAdapter em = PersistenceProvider.getEntityManager();
        QueryAdapter q = em.createQuery(
                "select u.id, u.name, p " +
                        " from sec$Profile p join p.user u" +
                        " where u.login = ?1 and u.password = ?2");
        q.setParameter(1, login);
        q.setParameter(2, password);
        List<Object[]> rows = q.getResultList();
        if (rows.isEmpty()) {
            log.warn("Failed to authenticate: " + login);
            throw new LoginException(Messages.getString("LoginException.InvalidLoginOrPassword", locale));
        }
        return rows;
    }

    public List<Profile> authenticate(String login, String password, Locale locale)
            throws LoginException
    {
        log.info("Authenticating: " + login);
        List<Object[]> rows = __authenticate(login, password, locale);

        List<Profile> profiles = new ArrayList<Profile>();
        for (Object[] row : rows) {
            profiles.add((Profile) row[2]);
        }
        return profiles;
//        Principal principal = SecurityAssociation.getCallerPrincipal();
//        char[] credential = (char[]) SecurityAssociation.getCredential();
//        log.debug("principal=" + principal.getName() + ", credential=" + String.valueOf(credential));
    }

    public UserSession login(String login, String password, String profileName, Locale locale)
            throws LoginException
    {
        if (profileName == null)
            throw new IllegalArgumentException("profileName can not be null");

        List<Object[]> rows = __authenticate(login, password, locale);
        Profile profile = null;
        UUID userId = null;
        String name = null;
        for (Object[] row : rows) {
            Profile p = (Profile) row[2];
            if (profileName.equals(p.getName())) {
                profile = p;
                userId = (UUID) row[0];
                name = (String) row[1];
                break;
            }
        }
        if (profile == null)
            throw new LoginException(Messages.getString("LoginException.InvalidProfile", locale), profileName);

        UserSession session = new UserSession();
        session.setId(UUID.randomUUID());
        session.setUserId(userId);
        session.setName(name);
        session.setLogin(login);

        log.info("Logged in: " + login);
        return session;
    }
}
