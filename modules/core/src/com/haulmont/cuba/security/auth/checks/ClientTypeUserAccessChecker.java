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

import com.haulmont.cuba.core.global.ClientType;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.security.auth.AbstractClientCredentials;
import com.haulmont.cuba.security.auth.Credentials;
import com.haulmont.cuba.security.auth.AuthenticationDetails;
import com.haulmont.cuba.security.global.LoginException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Locale;

/**
 * Checks if login to Desktop / Web client is permitted for user.
 */
@Component("cuba_ClientTypeUserAccessChecker")
public class ClientTypeUserAccessChecker extends AbstractUserAccessChecker implements Ordered {

    private final Logger log = LoggerFactory.getLogger(ClientTypeUserAccessChecker.class);

    @Inject
    protected Messages messages;

    @Inject
    public ClientTypeUserAccessChecker(Messages messages) {
        super(messages);
    }

    @Override
    public void check(Credentials credentials, AuthenticationDetails authenticationDetails) throws LoginException {
        if (credentials instanceof AbstractClientCredentials) {
            AbstractClientCredentials clientCredentials = (AbstractClientCredentials) credentials;

            if (clientCredentials.isCheckClientPermissions()) {
                ClientType clientType = clientCredentials.getClientType();

                if (ClientType.DESKTOP == clientType || ClientType.WEB == clientType) {
                    if (!authenticationDetails.getSession().isSpecificPermitted("cuba.gui.loginToClient")) {
                        log.warn("Attempt of login to {} for user '{}' without cuba.gui.loginToClient permission",
                                clientType, clientCredentials);

                        Locale userLocale;
                        if (clientCredentials.getLocale() != null) {
                            userLocale = clientCredentials.getLocale();
                        } else {
                            userLocale = messages.getTools().getDefaultLocale();
                        }

                        throw new LoginException(getInvalidCredentialsMessage(clientCredentials.getUserIdentifier(), userLocale));
                    }
                }
            }
        }
    }

    @Override
    public int getOrder() {
        return HIGHEST_PLATFORM_PRECEDENCE + 10;
    }
}