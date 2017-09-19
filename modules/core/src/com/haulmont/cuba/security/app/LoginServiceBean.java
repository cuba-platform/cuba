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

import com.haulmont.cuba.core.global.ClientType;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.sys.remoting.RemoteClientInfo;
import com.haulmont.cuba.security.auth.*;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.SessionParams;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/**
 * Service to provide methods for user login/logout to the middleware.
 */
@Deprecated
@Component(LoginService.NAME)
public class LoginServiceBean implements LoginService {

    private final Logger log = LoggerFactory.getLogger(LoginServiceBean.class);

    @Inject
    protected AuthenticationService authenticationService;

    @Inject
    protected TrustedClientService trustedClientService;

    @Inject
    protected LoginWorker loginWorker;

    @Inject
    protected BruteForceProtectionAPI bruteForceProtectionAPI;

    @Inject
    protected GlobalConfig globalConfig;

    @Override
    public UserSession login(String login, String password, Locale locale) throws LoginException {
        return login(login, password, locale, Collections.emptyMap());
    }

    @Override
    public UserSession login(String login, String password, Locale locale, Map<String, Object> params) throws LoginException {
        LoginPasswordCredentials credentials = new LoginPasswordCredentials(login, password, locale, params);
        copyParamsToCredentials(params, credentials);
        return authenticationService.login(credentials).getSession();
    }

    @Override
    public UserSession loginTrusted(String login, String password, Locale locale) throws LoginException {
        return loginTrusted(login, password, locale, Collections.emptyMap());
    }

    @Override
    public UserSession loginTrusted(String login, String password, Locale locale, Map<String, Object> params) throws LoginException {
        TrustedClientCredentials credentials = new TrustedClientCredentials(login, password, locale, params);
        RemoteClientInfo remoteClientInfo = RemoteClientInfo.get();
        if (remoteClientInfo != null) {
            credentials.setClientIpAddress(remoteClientInfo.getAddress());
        }
        copyParamsToCredentials(params, credentials);
        return authenticationService.login(credentials).getSession();
    }

    @Override
    public UserSession loginByRememberMe(String login, String rememberMeToken, Locale locale) throws LoginException {
        return loginByRememberMe(login, rememberMeToken, locale, Collections.emptyMap());
    }

    @Override
    public UserSession loginByRememberMe(String login, String rememberMeToken, Locale locale, Map<String, Object> params)
            throws LoginException {
        RememberMeCredentials credentials = new RememberMeCredentials(login, rememberMeToken, locale, params);
        copyParamsToCredentials(params, credentials);
        return authenticationService.login(credentials).getSession();
    }

    @Override
    public UserSession getSystemSession(String trustedClientPassword) throws LoginException {
        try {
            return trustedClientService.getSystemSession(trustedClientPassword);
        } catch (LoginException e) {
            log.info("Login failed: {}", e.toString());
            throw e;
        } catch (Throwable e) {
            log.error("Login error", e);
            //noinspection ThrowableResultOfMethodCallIgnored
            Throwable rootCause = ExceptionUtils.getRootCause(e);
            if (rootCause == null)
                rootCause = e;
            // send text only to avoid ClassNotFoundException when the client has no dependency to some library
            throw new LoginException(rootCause.toString());
        }
    }

    @Override
    public void logout() {
        authenticationService.logout();
    }

    @Override
    public UserSession substituteUser(User substitutedUser) {
        return authenticationService.substituteUser(substitutedUser);
    }

    @Override
    public UserSession getSession(UUID sessionId) {
        return loginWorker.getSession(sessionId);
    }

    @Override
    public boolean checkRememberMe(String login, String rememberMeToken) {
        log.warn("LoginService checkRememberMe is not supported any more. Always returns false");
        return false;
    }

    @Override
    public boolean isBruteForceProtectionEnabled() {
        // bruteForceProtectionEnabled is not accessible for clients any more
        return false;
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

    @Deprecated
    protected void copyParamsToCredentials(Map<String, Object> params, AbstractClientCredentials credentials) {
        // for compatibility only
        Object clientType = params.get(ClientType.class.getName());
        if (clientType != null) {
            credentials.setClientType(ClientType.valueOf((String) clientType));
        }
        Object clientInfo = params.get(SessionParams.CLIENT_INFO.getId());
        if (clientInfo != null) {
            credentials.setClientInfo((String) clientInfo);
        }
        Object ipAddress = params.get(SessionParams.IP_ADDERSS.getId());
        if (ipAddress != null) {
            credentials.setIpAddress((String) ipAddress);
        }
        Object hostName = params.get(SessionParams.HOST_NAME.getId());
        if (hostName != null) {
            credentials.setHostName((String) hostName);
        }
        if (!globalConfig.getLocaleSelectVisible()) {
            credentials.setOverrideLocale(false);
        }
    }
}