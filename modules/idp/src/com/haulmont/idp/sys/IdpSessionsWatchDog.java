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
 */

package com.haulmont.idp.sys;

import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.security.app.TrustedClientService;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.idp.IdpService;
import com.haulmont.idp.IdpConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.List;

/**
 * Invokes IdpService to check if IDP sessions or tickets are expired. <br>
 * Performs request to logout URLs of service providers registered in {@link IdpConfig#getServiceProviderLogoutUrls()}.
 */
@Component("cuba_IdpSessionsWatchDog")
public class IdpSessionsWatchDog {

    private final Logger log = LoggerFactory.getLogger(IdpSessionsWatchDog.class);

    @Inject
    protected TrustedClientService trustedClientService;

    @Inject
    protected IdpService idpService;

    @Inject
    protected IdpConfig idpConfig;

    @Inject
    protected IdpServiceLogoutCallbackInvoker logoutCallbackInvoker;

    public void cleanupExpiredSessions() {
        if (!AppContext.isStarted()) {
            return;
        }

        List<String> serviceProviderUrls = idpConfig.getServiceProviderUrls();
        if (serviceProviderUrls.isEmpty()) {
            // there are no service providers registered
            return;
        }

        UserSession systemSession;
        try {
            systemSession = trustedClientService.getSystemSession(idpConfig.getTrustedClientPassword());
        } catch (LoginException e) {
            log.error("Unable to obtain system session", e);
            return;
        }

        AppContext.withSecurityContext(new SecurityContext(systemSession), () -> {
            List<String> loggedOutIdpSessionIds = idpService.processEviction(
                    idpConfig.getSessionExpirationTimeoutSec(),
                    idpConfig.getTicketExpirationTimeoutSec()
            );

            for (String idpSessionId : loggedOutIdpSessionIds) {
                logoutCallbackInvoker.performLogoutOnServiceProviders(idpSessionId);
            }
        });
    }
}