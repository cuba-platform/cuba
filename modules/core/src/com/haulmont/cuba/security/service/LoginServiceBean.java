/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 26.11.2008 13:30:56
 *
 * $Id$
 */
package com.haulmont.cuba.security.service;

import com.haulmont.cuba.core.Locator;
import com.haulmont.cuba.security.entity.Profile;
import com.haulmont.cuba.security.global.LoginServiceRemote;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.worker.LoginWorker;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.Stateless;
import java.util.List;
import java.util.Locale;

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
            log.error(e);
            throw new RuntimeException(e);
        }
    }

    public UserSession login(String login, String password, String profileName, Locale locale) throws LoginException {
        try {
            return getLoginWorker().login(login, password, profileName, null);
        } catch (Exception e) {
            log.error(e);
            throw new RuntimeException(e);
        }
    }

    public void logout() {
        getLoginWorker().logout();
    }
}
