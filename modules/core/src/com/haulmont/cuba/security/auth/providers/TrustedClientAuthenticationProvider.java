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
import com.haulmont.cuba.security.auth.*;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.sys.TrustedLoginHandler;
import com.haulmont.cuba.security.sys.UserSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.List;
import java.util.Locale;

@Component("cuba_TrustedClientAuthenticationProvider")
public class TrustedClientAuthenticationProvider extends AbstractAuthenticationProvider implements Ordered {

    private final Logger log = LoggerFactory.getLogger(TrustedClientAuthenticationProvider.class);

    @Inject
    protected List<UserAccessChecker> userAccessCheckers;
    @Inject
    protected UserSessionManager userSessionManager;
    @Inject
    protected TrustedLoginHandler trustedLoginHandler;

    @Inject
    public TrustedClientAuthenticationProvider(Persistence persistence, Messages messages) {
        super(persistence, messages);
    }

    @Override
    public AuthenticationDetails authenticate(Credentials credentials) throws LoginException {
        TrustedClientCredentials trustedClient = (TrustedClientCredentials) credentials;

        String login = trustedClient.getLogin();

        Locale credentialsLocale = trustedClient.getLocale() == null ?
                messages.getTools().getDefaultLocale() : trustedClient.getLocale();

        if (Strings.isNullOrEmpty(login)) {
            throw new LoginException(getInvalidCredentialsMessage(login, credentialsLocale));
        }

        User user = loadUser(login);
        if (user == null) {
            throw new LoginException(getInvalidCredentialsMessage(login, credentialsLocale));
        }

        if (trustedClient.getClientIpAddress() != null) {
            // reject request from not permitted client IP
            if (!trustedLoginHandler.checkAddress(trustedClient.getClientIpAddress())) {
                log.warn("Attempt of trusted login from not permitted IP address: {} {}", login, trustedClient.getClientIpAddress());
                throw new LoginException(getInvalidCredentialsMessage(login, credentialsLocale));
            }
        } else {
            log.trace("Unable to check trusted client IP for user {}. It is OK in case of local service invocation mode",
                    trustedClient.getLogin());
        }

        if (!trustedLoginHandler.checkPassword(trustedClient.getTrustedClientPassword())) {
            throw new LoginException(getInvalidCredentialsMessage(login, credentialsLocale));
        }

        Locale userLocale = getUserLocale(trustedClient, user);

        UserSession session = createSession(trustedClient, user, userLocale);

        setClientSessionParams(trustedClient, session);

        AuthenticationDetails authenticationDetails = new SimpleAuthenticationDetails(session);

        checkUserAccess(trustedClient, authenticationDetails);

        return authenticationDetails;
    }

    @SuppressWarnings("RedundantThrows")
    protected UserSession createSession(@SuppressWarnings("unused") TrustedClientCredentials credentials,
                                        User user, Locale userLocale) throws LoginException {
        return userSessionManager.createSession(user, userLocale, false, credentials.getSecurityScope());
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
        return TrustedClientCredentials.class.isAssignableFrom(credentialsClass);
    }

    @Override
    public int getOrder() {
        return HIGHEST_PLATFORM_PRECEDENCE + 30;
    }
}