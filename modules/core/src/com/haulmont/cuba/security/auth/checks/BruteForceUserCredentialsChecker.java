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

package com.haulmont.cuba.security.auth.checks;

import com.haulmont.cuba.core.global.Events;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.security.app.BruteForceProtectionAPI;
import com.haulmont.cuba.security.auth.AbstractClientCredentials;
import com.haulmont.cuba.security.auth.Credentials;
import com.haulmont.cuba.security.auth.UserCredentialsChecker;
import com.haulmont.cuba.security.auth.events.AuthenticationFailureEvent;
import com.haulmont.cuba.security.global.AccountLockedException;
import com.haulmont.cuba.security.global.BadCredentialsException;
import com.haulmont.cuba.security.global.LoginException;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Locale;

/**
 * Checks if a user tries to brute-force password / remember me token.
 */
@Component("cuba_BruteForceUserCredentialsChecker")
public class BruteForceUserCredentialsChecker implements UserCredentialsChecker, Ordered {

    protected static final String MSG_PACK = "com.haulmont.cuba.security";

    @Inject
    protected Messages messages;
    @Inject
    protected BruteForceProtectionAPI bruteForceProtectionAPI;

    @Override
    public void check(Credentials credentials) throws LoginException {
        if (bruteForceProtectionAPI.isBruteForceProtectionEnabled()) {
            if (credentials instanceof AbstractClientCredentials) {
                AbstractClientCredentials clientCredentials = (AbstractClientCredentials) credentials;

                if (clientCredentials.isCheckClientPermissions()
                        && clientCredentials.getIpAddress() != null
                        && bruteForceProtectionAPI.loginAttemptsLeft(clientCredentials.getUserIdentifier(),
                        clientCredentials.getIpAddress()) <= 0) {

                    Locale locale = clientCredentials.getLocale() == null
                            ? messages.getTools().getDefaultLocale()
                            : clientCredentials.getLocale();

                    String message = messages.formatMessage(MSG_PACK,
                            "LoginException.loginAttemptsNumberExceeded",
                            locale,
                            bruteForceProtectionAPI.getBruteForceBlockIntervalSec());

                    throw new AccountLockedException(message);
                }
            }
        }
    }

    @Order(Events.HIGHEST_PLATFORM_PRECEDENCE + 10)
    @EventListener
    protected void onAuthenticationFailure(AuthenticationFailureEvent event) throws LoginException {
        if (bruteForceProtectionAPI.isBruteForceProtectionEnabled() &&
                event.getException() instanceof BadCredentialsException) {
            Credentials credentials = event.getCredentials();
            if (credentials instanceof AbstractClientCredentials) {
                AbstractClientCredentials clientCredentials = (AbstractClientCredentials) credentials;

                if (clientCredentials.isCheckClientPermissions()) {
                    bruteForceProtectionAPI.registerUnsuccessfulLogin(
                            clientCredentials.getUserIdentifier(), clientCredentials.getIpAddress());
                }
            }
        }
    }

    @Override
    public int getOrder() {
        return HIGHEST_PLATFORM_PRECEDENCE + 10;
    }
}