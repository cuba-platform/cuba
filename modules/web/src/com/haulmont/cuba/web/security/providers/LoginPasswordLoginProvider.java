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

import com.haulmont.cuba.core.global.PasswordEncryption;
import com.haulmont.cuba.security.auth.AuthenticationDetails;
import com.haulmont.cuba.security.auth.AuthenticationService;
import com.haulmont.cuba.security.auth.Credentials;
import com.haulmont.cuba.security.auth.LoginPasswordCredentials;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.web.security.LoginProvider;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;

@Component("cuba_LoginPasswordLoginProvider")
public class LoginPasswordLoginProvider implements LoginProvider, Ordered {
    @Inject
    protected AuthenticationService authenticationService;
    @Inject
    protected PasswordEncryption passwordEncryption;

    @Nullable
    @Override
    public AuthenticationDetails login(Credentials credentials) throws LoginException {
        LoginPasswordCredentials loginPasswordCredentials = (LoginPasswordCredentials) credentials;

        if (loginPasswordCredentials.getPassword() != null) {
            String hashedPassword = passwordEncryption.getPlainHash(loginPasswordCredentials.getPassword());
            loginPasswordCredentials.setPassword(hashedPassword);
        }

        return loginMiddleware(loginPasswordCredentials);
    }

    protected AuthenticationDetails loginMiddleware(LoginPasswordCredentials credentials) throws LoginException {
        return authenticationService.login(credentials);
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