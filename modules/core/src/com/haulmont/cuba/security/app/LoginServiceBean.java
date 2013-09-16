/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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
import java.util.Map;
import java.util.UUID;

/**
 * Service to provide methods for user login/logout to the middleware.
 *
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean(LoginService.NAME)
public class LoginServiceBean implements LoginService {

    protected Log log = LogFactory.getLog(getClass());

    @Inject
    protected LoginWorker loginWorker;

    @Override
    public UserSession login(String login, String password, Locale locale) throws LoginException {
        try {
            return loginWorker.login(login, password, locale);
        } catch (LoginException e) {
            log.info("Login failed: " + e.toString());
            throw e;
        } catch (RuntimeException | Error e) {
            log.error("Login error", e);
            throw e;
        }
    }

    @Override
    public UserSession login(String login, String password, Locale locale, Map<String, Object> params) throws LoginException {
        try {
            return loginWorker.login(login, password, locale, params);
        } catch (LoginException e) {
            log.info("Login failed: " + e.toString());
            throw e;
        } catch (RuntimeException | Error e) {
            log.error("Login error", e);
            throw e;
        }
    }

    @Override
    public UserSession loginTrusted(String login, String password, Locale locale) throws LoginException {
        try {
            return loginWorker.loginTrusted(login, password, locale);
        } catch (LoginException e) {
            log.info("Login failed: " + e.toString());
            throw e;
        } catch (RuntimeException | Error e) {
            log.error("Login error", e);
            throw e;
        }
    }

    @Override
    public UserSession loginTrusted(String login, String password, Locale locale, Map<String, Object> params) throws LoginException {
        try {
            return loginWorker.loginTrusted(login, password, locale, params);
        } catch (LoginException e) {
            log.info("Login failed: " + e.toString());
            throw e;
        } catch (RuntimeException | Error e) {
            log.error("Login error", e);
            throw e;
        }
    }

    @Override
    public void logout() {
        try {
            loginWorker.logout();
        } catch (RuntimeException | Error e) {
            log.error("Logout error", e);
            throw e;
        }
    }

    @Override
    public UserSession substituteUser(User substitutedUser) {
        return loginWorker.substituteUser(substitutedUser);
    }

    @Override
    public UserSession getSession(UUID sessionId) {
        return loginWorker.getSession(sessionId);
    }
}
