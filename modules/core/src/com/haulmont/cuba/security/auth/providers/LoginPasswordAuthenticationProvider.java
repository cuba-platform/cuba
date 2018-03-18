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

package com.haulmont.cuba.security.auth.providers;

import com.google.common.base.Strings;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.PasswordEncryption;
import com.haulmont.cuba.security.auth.*;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.sys.UserSessionManager;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.List;
import java.util.Locale;

@Component("cuba_LoginPasswordAuthenticationProvider")
public class LoginPasswordAuthenticationProvider extends AbstractAuthenticationProvider implements Ordered {
    @Inject
    protected List<UserAccessChecker> userAccessCheckers;
    @Inject
    protected List<UserCredentialsChecker> userCredentialsCheckers;
    @Inject
    protected UserSessionManager userSessionManager;
    @Inject
    protected PasswordEncryption passwordEncryption;

    @Inject
    public LoginPasswordAuthenticationProvider(Persistence persistence, Messages messages) {
        super(persistence, messages);
    }

    @Override
    public AuthenticationDetails authenticate(Credentials credentials) throws LoginException {
        LoginPasswordCredentials loginAndPassword = (LoginPasswordCredentials) credentials;

        String login = loginAndPassword.getLogin();

        Locale credentialsLocale = loginAndPassword.getLocale() == null ?
                messages.getTools().getDefaultLocale() : loginAndPassword.getLocale();

        if (Strings.isNullOrEmpty(login)) {
            // empty login is not valid
            throw new LoginException(getInvalidCredentialsMessage(login, credentialsLocale));
        }

        checkUserCredentials(credentials);

        User user = loadUser(login);
        if (user == null) {
            throw new LoginException(getInvalidCredentialsMessage(login, credentialsLocale));
        }

        if (!passwordEncryption.checkPassword(user, loginAndPassword.getPassword())) {
            throw new LoginException(getInvalidCredentialsMessage(login, credentialsLocale));
        }

        Locale userLocale = getUserLocale(loginAndPassword, user);

        UserSession session = createSession(loginAndPassword, user, userLocale);

        setClientSessionParams(loginAndPassword, session);

        AuthenticationDetails authenticationDetails = new SimpleAuthenticationDetails(session);

        checkUserAccess(loginAndPassword, authenticationDetails);

        return authenticationDetails;
    }

    @SuppressWarnings("RedundantThrows")
    protected UserSession createSession(@SuppressWarnings("unused") LoginPasswordCredentials credentials,
                                        User user, Locale userLocale) throws LoginException {
        return userSessionManager.createSession(user, userLocale, false);
    }

    protected void checkUserCredentials(Credentials credentials) throws LoginException {
        if (userCredentialsCheckers != null) {
            for (UserCredentialsChecker checker : userCredentialsCheckers) {
                checker.check(credentials);
            }
        }
    }

    protected void checkUserAccess(Credentials loginAndPassword, AuthenticationDetails authenticationDetails)
            throws LoginException {
        if (userAccessCheckers != null) {
            for (UserAccessChecker checker : userAccessCheckers) {
                checker.check(loginAndPassword, authenticationDetails);
            }
        }
    }

    @Override
    public boolean supports(Class<?> credentialsClass) {
        return LoginPasswordCredentials.class.isAssignableFrom(credentialsClass);
    }

    @Override
    public int getOrder() {
        return HIGHEST_PLATFORM_PRECEDENCE + 10;
    }
}