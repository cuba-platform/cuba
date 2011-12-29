/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.security.app;

import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.util.Locale;
import java.util.UUID;

/**
 * Service to provide methods for user login/logout to the middleware.
 *
 * @version $Id$
 *
 * @author krivopustov
 */
@ManagedBean(LoginService.NAME)
public class LoginServiceBean implements LoginService
{
    private Log log = LogFactory.getLog(LoginServiceBean.class);

    private LoginWorker loginWorker;

    @Inject
    public void setLoginWorker(LoginWorker loginWorker) {
        this.loginWorker = loginWorker;
    }

    @Override
    public UserSession login(String login, String password, Locale locale) throws LoginException {
        try {
            return loginWorker.login(login, password, locale);
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

    @Override
    public UserSession loginTrusted(String login, String password, Locale locale) throws LoginException {
        try {
            return loginWorker.loginTrusted(login, password, locale);
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

    @Override
    public void logout() {
        try {
            loginWorker.logout();
        } catch (Exception e) {
            log.error("Logout error", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserSession substituteUser(User substitutedUser) {
        return loginWorker.substituteUser(substitutedUser);
    }

    @Override
    public void ping() {
        log.debug("ping");
    }

    @Override
    public UserSession getSession(UUID sessionId) {
        return loginWorker.getSession(sessionId);
    }
}
