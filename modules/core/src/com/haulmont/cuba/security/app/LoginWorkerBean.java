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

import com.haulmont.cuba.core.app.ServerConfig;
import com.haulmont.cuba.core.global.ClientType;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.security.auth.*;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.SessionParams;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.sys.TrustedLoginHandler;
import com.haulmont.cuba.security.sys.UserSessionManager;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/**
 * Class that encapsulates the middleware login/logout functionality.
 *
 * @see LoginServiceBean
 */
@Component(LoginWorker.NAME)
@Deprecated
public class LoginWorkerBean implements LoginWorker {

    protected static final String MSG_PACK = "com.haulmont.cuba.security";

    @Inject
    protected Messages messages;

    @Inject
    protected ServerConfig serverConfig;

    @Inject
    protected GlobalConfig globalConfig;

    @Inject
    protected UserSessionManager userSessionManager;

    @Inject
    protected TrustedLoginHandler trustedLoginHandler;

    @Inject
    protected Authentication authentication;

    @Inject
    protected AnonymousSessionHolder anonymousSessionHolder;

    @Inject
    protected AuthenticationManager authenticationManager;

    @Override
    public UserSession login(String login, String password, Locale locale) throws LoginException {
        return login(login, password, locale, Collections.emptyMap());
    }

    @Override
    public UserSession login(String login, String password, Locale locale, Map<String, Object> params) throws LoginException {
        return authenticationManager.login(new LoginPasswordCredentials(login, password, locale, params)).getSession();
    }

    @Override
    public UserSession loginSystem(String login) throws LoginException {
        return authenticationManager.login(new SystemUserCredentials(login)).getSession();
    }

    @Override
    public UserSession loginAnonymous() throws LoginException {
        return anonymousSessionHolder.getAnonymousSession();
    }

    @Override
    public UserSession getSystemSession(String trustedClientPassword) throws LoginException {
        if (!trustedLoginHandler.checkPassword(trustedClientPassword)) {
            Locale locale = messages.getTools().getDefaultLocale();
            throw new LoginException(messages.formatMessage(MSG_PACK, "LoginException.InvalidLoginOrPassword", locale,
                    serverConfig.getJmxUserLogin()));
        }

        SecurityContext currentSecContext = AppContext.getSecurityContext();
        UserSession userSession;
        try {
            // we need to reset security context to prevent reusing current session
            AppContext.setSecurityContext(null);

            userSession = authentication.begin();
            authentication.end();
        } finally {
            AppContext.setSecurityContext(currentSecContext);
        }

        return userSession;
    }

    @Override
    public UserSession loginTrusted(String login, String password, Locale locale) throws LoginException {
        return loginTrusted(login, password, locale, Collections.emptyMap());
    }

    @Override
    public UserSession loginTrusted(String login, String password, Locale locale, Map<String, Object> params) throws LoginException {
        TrustedClientCredentials credentials = new TrustedClientCredentials(login, password, locale, params);
        copyParamsToCredentials(params, credentials);
        return authenticationManager.login(credentials).getSession();
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
        return authenticationManager.login(credentials).getSession();
    }

    @Override
    public void logout() {
        authenticationManager.logout();
    }

    @Override
    public UserSession substituteUser(User substitutedUser) {
        return authenticationManager.substituteUser(substitutedUser);
    }

    @Override
    public UserSession getSession(UUID sessionId) {
        return userSessionManager.findSession(sessionId);
    }

    @Override
    public boolean checkRememberMe(String login, String rememberMeToken) {
        // always return false, this feature is not supported any more
        return false;
    }

    @Override
    public User authenticate(String login, String password, Locale locale, Map<String, Object> params)
            throws LoginException {
        LoginPasswordCredentials credentials = new LoginPasswordCredentials(login, password, locale, params);
        copyParamsToCredentials(params, credentials);
        AuthenticationDetails sessionDetails = authenticationManager.authenticate(credentials);
        return sessionDetails.getSession().getUser();
    }

    @Deprecated
    protected void copyParamsToCredentials(Map<String, Object> params, AbstractClientCredentials credentials) {
        // for compatibility only
        Object clientType = params.get(ClientType.class.getName());
        if (clientType != null && credentials.getClientType() == null) {
            credentials.setClientType(ClientType.valueOf((String) clientType));
        }
        Object clientInfo = params.get(SessionParams.CLIENT_INFO.getId());
        if (clientInfo != null && credentials.getClientInfo() == null) {
            credentials.setClientInfo((String) clientInfo);
        }
        Object ipAddress = params.get(SessionParams.IP_ADDERSS.getId());
        if (ipAddress != null && credentials.getIpAddress() == null) {
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