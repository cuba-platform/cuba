/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 26.11.2008 13:30:56
 *
 * $Id$
 */
package com.haulmont.cuba.security.app;

import com.haulmont.cuba.core.Locator;
import com.haulmont.cuba.security.entity.Profile;
import com.haulmont.cuba.security.global.LoginServiceRemote;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.app.LoginWorker;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.security.SecurityAssociation;

import javax.ejb.Stateless;
import java.util.List;
import java.util.Locale;
import java.security.Principal;

@Stateless(name = LoginServiceRemote.JNDI_NAME)
public class LoginServiceBean implements LoginService, LoginServiceRemote
{
    private Log log = LogFactory.getLog(LoginServiceBean.class);

    private LoginWorker getLoginWorker() {
        return Locator.lookupLocal(LoginWorker.JNDI_NAME);
    }

    public List<Profile> authenticate(String login, String password, Locale locale) throws LoginException {
        try {
            return getLoginWorker().authenticate(login, password, null);
        } catch (Exception e) {
            log.error("Authentication error", e);
            throw new RuntimeException(e);
        }
    }

    public UserSession login(String login, String password, String profileName, Locale locale) throws LoginException {
        try {
            return getLoginWorker().login(login, password, profileName, null);
        } catch (Exception e) {
            log.error("Login error", e);
            throw new RuntimeException(e);
        }
    }

    public void logout() {
        try {
            getLoginWorker().logout();
        } catch (Exception e) {
            log.error("Logout error", e);
            throw new RuntimeException(e);
        }
    }

    public void ping() {
        Principal principal = SecurityAssociation.getPrincipal();
        Object credential = SecurityAssociation.getCredential();
        log.debug(principal + " " + credential);
    }
}
