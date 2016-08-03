/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.haulmont.cuba.security.app;

import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/**
 * Service to provide methods for user login/logout to the middleware.
 */
@Component(LoginService.NAME)
public class LoginServiceBean implements LoginService {

    private final Logger log = LoggerFactory.getLogger(LoginServiceBean.class);

    @Inject
    protected LoginWorker loginWorker;

    @Inject
    protected UserSessionSource userSessionSource;

    @Inject
    protected BruteForceProtectionAPI bruteForceProtectionAPI;

    @Override
    public UserSession login(String login, String password, Locale locale) throws LoginException {
        try {
            return loginWorker.login(login, password, locale);
        } catch (LoginException e) {
            log.info("Login failed: {}", e.toString());
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
            log.info("Login failed: {}", e.toString());
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
            log.info("Login failed: {}", e.toString());
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
            log.info("Login failed: {}", e.toString());
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
            log.info("Login failed: {}", e.toString());
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
            log.info("Login failed: {}", e.toString());
            throw e;
        } catch (Throwable e) {
            log.error("Login error", e);
            throw wrapInLoginException(e);
        }
    }

    @Override
    public UserSession getSystemSession(String trustedClientPassword) throws LoginException {
        try {
            return loginWorker.getSystemSession(trustedClientPassword);
        } catch (LoginException e) {
            log.info("Login failed: {}", e.toString());
            throw e;
        } catch (Throwable e) {
            log.error("Login error", e);
            throw wrapInLoginException(e);
        }
    }

    @Override
    public void logout() {
        try {
            UserSession session = userSessionSource.getUserSession();

            if (session != null && session.isSystem()) {
                throw new RuntimeException("Logout of system session from client is not permitted");
            }

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


    @Override
    public boolean isBruteForceProtectionEnabled() {
        return bruteForceProtectionAPI.isBruteForceProtectionEnabled();
    }

    @Override
    public int getBruteForceBlockIntervalSec() {
        return bruteForceProtectionAPI.getBruteForceBlockIntervalSec();
    }

    @Override
    public int loginAttemptsLeft(String login, String ipAddress) {
        return bruteForceProtectionAPI.loginAttemptsLeft(login, ipAddress);
    }

    @Override
    public int registerUnsuccessfulLogin(String login, String ipAddress) {
        return bruteForceProtectionAPI.registerUnsuccessfulLogin(login, ipAddress);
    }
}