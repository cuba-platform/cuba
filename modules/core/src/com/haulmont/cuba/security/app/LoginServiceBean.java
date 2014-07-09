/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.security.app;

import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.lang.exception.ExceptionUtils;
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
        } catch (Throwable e) {
            log.error("Login error", e);
            throw wrapInLoginException(e);
        }
    }

    @Override
    public UserSession login(String login, String password, Locale locale, Map<String, Object> params) throws LoginException {
        try {
            return loginWorker.login(login, password, locale, params);
        } catch (LoginException e) {
            log.info("Login failed: " + e.toString());
            throw e;
        } catch (Throwable e) {
            log.error("Login error", e);
            throw wrapInLoginException(e);
        }
    }

    @Override
    public UserSession loginTrusted(String login, String password, Locale locale) throws LoginException {
        try {
            return loginWorker.loginTrusted(login, password, locale);
        } catch (LoginException e) {
            log.info("Login failed: " + e.toString());
            throw e;
        } catch (Throwable e) {
            log.error("Login error", e);
            throw wrapInLoginException(e);
        }
    }

    @Override
    public UserSession loginTrusted(String login, String password, Locale locale, Map<String, Object> params) throws LoginException {
        try {
            return loginWorker.loginTrusted(login, password, locale, params);
        } catch (LoginException e) {
            log.info("Login failed: " + e.toString());
            throw e;
        } catch (Throwable e) {
            log.error("Login error", e);
            throw wrapInLoginException(e);
        }
    }

    @Override
    public UserSession loginByRememberMe(String login, String rememberMeToken, Locale locale) throws LoginException {
        try {
            return loginWorker.loginByRememberMe(login, rememberMeToken, locale);
        } catch (LoginException e) {
            log.info("Login failed: " + e.toString());
            throw e;
        } catch (Throwable e) {
            log.error("Login error", e);
            throw wrapInLoginException(e);
        }
    }

    @Override
    public UserSession loginByRememberMe(String login, String rememberMeToken, Locale locale, Map<String, Object> params)
            throws LoginException {
        try {
            return loginWorker.loginByRememberMe(login, rememberMeToken, locale, params);
        } catch (LoginException e) {
            log.info("Login failed: " + e.toString());
            throw e;
        } catch (Throwable e) {
            log.error("Login error", e);
            throw wrapInLoginException(e);
        }
    }

    @Override
    public void logout() {
        try {
            loginWorker.logout();
        } catch (Throwable e) {
            log.error("Logout error", e);
            throw new RuntimeException(e.toString());
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

    @Override
    public boolean checkRememberMe(String login, String rememberMeToken) {
        return loginWorker.checkRememberMe(login, rememberMeToken);
    }

    protected LoginException wrapInLoginException(Throwable throwable) {
        //noinspection ThrowableResultOfMethodCallIgnored
        Throwable rootCause = ExceptionUtils.getRootCause(throwable);
        if (rootCause == null)
            rootCause = throwable;
        // send text only to avoid ClassNotFoundException when the client has no dependency to some library
        return new LoginException(rootCause.toString());
    }
}