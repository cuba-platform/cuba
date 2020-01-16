/*
 * Copyright (c) 2008-2017 Haulmont.
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
 */

package com.haulmont.cuba.web.security.providers;

import com.google.common.base.Strings;
import com.haulmont.cuba.client.sys.UsersRepository;
import com.haulmont.cuba.core.global.ClientType;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.PasswordEncryption;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.security.app.TrustedClientService;
import com.haulmont.cuba.security.auth.*;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.auth.WebAuthConfig;
import com.haulmont.cuba.web.security.LoginProvider;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Locale;

@Component("cuba_LoginPasswordLoginProvider")
public class LoginPasswordLoginProvider implements LoginProvider, Ordered {
    @Inject
    protected AuthenticationService authenticationService;
    @Inject
    protected Messages messages;
    @Inject
    protected UsersRepository usersRepository;
    @Inject
    protected PasswordEncryption passwordEncryption;
    @Inject
    protected TrustedClientService trustedClientService;
    @Inject
    protected WebAuthConfig webAuthConfig;

    protected static final String MSG_PACK = "com.haulmont.cuba.security";

    @Nullable
    @Override
    public AuthenticationDetails login(Credentials credentials) throws LoginException {
        LoginPasswordCredentials loginPasswordCredentials = (LoginPasswordCredentials) credentials;
        if (webAuthConfig.getCheckPasswordOnClient()) {
            return loginClient(loginPasswordCredentials);
        } else {
            return loginMiddleware(loginPasswordCredentials);
        }
    }

    protected AuthenticationDetails loginMiddleware(LoginPasswordCredentials credentials) throws LoginException {
        return authenticationService.login(credentials);
    }

    protected AuthenticationDetails loginClient(LoginPasswordCredentials credentials) {
        String login = credentials.getLogin();

        Locale credentialsLocale = credentials.getLocale() == null ?
                messages.getTools().getDefaultLocale() : credentials.getLocale();

        if (Strings.isNullOrEmpty(login)) {
            // empty login is not valid
            throw new LoginException(getInvalidCredentialsMessage(login, credentialsLocale));
        }

        UserSession systemSession = trustedClientService.getSystemSession(webAuthConfig.getTrustedClientPassword());
        User user = AppContext.withSecurityContext(new SecurityContext(systemSession), () -> usersRepository.findUserByLogin(login));

        if (user == null) {
            throw new LoginException(getInvalidCredentialsMessage(login, credentialsLocale));
        }

        if (!passwordEncryption.checkPassword(user, credentials.getPassword())) {
            throw new LoginException(getInvalidCredentialsMessage(login, credentialsLocale));
        }

        return authenticationService.login(createTrustedCredentials(credentials));
    }

    protected TrustedClientCredentials createTrustedCredentials(LoginPasswordCredentials credentials) {
        TrustedClientCredentials tcCredentials = new TrustedClientCredentials(
                credentials.getLogin(),
                webAuthConfig.getTrustedClientPassword(),
                credentials.getLocale(),
                credentials.getParams()
        );

        tcCredentials.setClientInfo(credentials.getClientInfo());
        tcCredentials.setClientType(ClientType.WEB);
        tcCredentials.setIpAddress(credentials.getIpAddress());
        tcCredentials.setOverrideLocale(credentials.isOverrideLocale());
        tcCredentials.setSyncNewUserSessionReplication(credentials.isSyncNewUserSessionReplication());
        tcCredentials.setSessionAttributes(credentials.getSessionAttributes());
        tcCredentials.setSecurityScope(webAuthConfig.getSecurityScope());

        return tcCredentials;
    }

    protected String getInvalidCredentialsMessage(String login, Locale locale) {
        return messages.formatMessage(MSG_PACK, "LoginException.InvalidLoginOrPassword", locale, login);
    }

    @Override
    public boolean supports(Class<?> credentialsClass) {
        return LoginPasswordCredentials.class.isAssignableFrom(credentialsClass);
    }

    @Override
    public int getOrder() {
        return HIGHEST_PLATFORM_PRECEDENCE + 60;
    }
}