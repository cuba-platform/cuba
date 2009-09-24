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
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.LoginServiceRemote;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.entity.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.Stateless;
import java.util.Locale;

/**
 * Service providing methods for user login/logout to the middleware
 */
@Stateless(name = LoginServiceRemote.JNDI_NAME)
public class LoginServiceBean implements LoginService, LoginServiceRemote
{
    private Log log = LogFactory.getLog(LoginServiceBean.class);

    private LoginWorker getLoginWorker() {
        return Locator.lookupLocal(LoginWorker.JNDI_NAME);
    }

    public UserSession login(String login, String password, Locale locale) throws LoginException {
        try {
            return getLoginWorker().login(login, password, locale);
        } catch (Exception e) {
            log.error("Login error", e);
            if (e instanceof LoginException)
                throw ((LoginException) e);
            else if (e instanceof RuntimeException)
                throw ((RuntimeException) e);
            else
                throw new RuntimeException(e);
        }
    }

    public UserSession loginActiveDirectory(String login, Locale locale) throws LoginException {
        try {
            return getLoginWorker().loginActiveDirectory(login, locale);
        } catch (Exception e) {
            log.error("Login error", e);
            if (e instanceof LoginException)
                throw ((LoginException) e);
            else if (e instanceof RuntimeException)
                throw ((RuntimeException) e);
            else
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

    public UserSession substituteUser(User substitutedUser) {
        return getLoginWorker().substituteUser(substitutedUser);
    }

    public void ping() {
        log.debug("ping");
    }
}
