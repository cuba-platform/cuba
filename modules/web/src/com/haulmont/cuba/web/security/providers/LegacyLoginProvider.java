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

import com.google.common.collect.ImmutableMap;
import com.haulmont.cuba.core.global.ClientType;
import com.haulmont.cuba.core.sys.ConditionalOnAppProperty;
import com.haulmont.cuba.security.auth.*;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.web.auth.CubaAuthProvider;
import com.haulmont.cuba.web.auth.DomainAliasesResolver;
import com.haulmont.cuba.web.auth.WebAuthConfig;
import com.haulmont.cuba.web.security.LoginProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static com.haulmont.cuba.web.security.ExternalUserCredentials.EXTERNAL_AUTH_USER_SESSION_ATTRIBUTE;

@Deprecated
@ConditionalOnAppProperty(property = "cuba.web.externalAuthentication", value = "true")
@Component("cuba_LegacyLoginProvider")
public class LegacyLoginProvider implements LoginProvider, Ordered {

    private final Logger log = LoggerFactory.getLogger(LegacyLoginProvider.class);

    @Inject
    protected AuthenticationService authenticationService;
    @Inject
    protected CubaAuthProvider authProvider;
    @Inject
    protected WebAuthConfig webAuthConfig;
    @Inject
    protected DomainAliasesResolver domainAliasesResolver;

    @Nullable
    @Override
    public AuthenticationDetails login(Credentials credentials) throws LoginException {
        LoginPasswordCredentials loginPassword = (LoginPasswordCredentials) credentials;

        if (webAuthConfig.getStandardAuthenticationUsers().contains(loginPassword.getLogin())) {
            log.debug("User {} is not allowed to use external login");
            return null;
        }

        authProvider.authenticate(loginPassword.getLogin(), loginPassword.getPassword(), loginPassword.getLocale());

        String systemLogin = convertLoginString(loginPassword.getLogin());

        TrustedClientCredentials trustedClientCredentials = new TrustedClientCredentials(
                systemLogin,
                webAuthConfig.getTrustedClientPassword(),
                loginPassword.getLocale(),
                loginPassword.getParams()
        );

        trustedClientCredentials.setClientInfo(loginPassword.getClientInfo());
        trustedClientCredentials.setClientType(ClientType.WEB);
        trustedClientCredentials.setIpAddress(loginPassword.getIpAddress());
        trustedClientCredentials.setOverrideLocale(loginPassword.isOverrideLocale());
        trustedClientCredentials.setSyncNewUserSessionReplication(loginPassword.isSyncNewUserSessionReplication());

        Map<String, Serializable> sessionAttributes = loginPassword.getSessionAttributes();
        Map<String, Serializable> targetSessionAttributes;
        if (sessionAttributes != null
                && !sessionAttributes.isEmpty()) {
            targetSessionAttributes = new HashMap<>(sessionAttributes);
            targetSessionAttributes.put(EXTERNAL_AUTH_USER_SESSION_ATTRIBUTE, true);
        } else {
            targetSessionAttributes = ImmutableMap.of(EXTERNAL_AUTH_USER_SESSION_ATTRIBUTE, true);
        }
        trustedClientCredentials.setSessionAttributes(targetSessionAttributes);

        return authenticationService.login(trustedClientCredentials);
    }

    /**
     * Convert userName to db form
     * In database users stores in form DOMAIN&#92;userName
     *
     * @param login Login string
     * @return login in form DOMAIN&#92;userName
     */
    protected String convertLoginString(String login) {
        int slashPos = login.indexOf("\\");
        if (slashPos >= 0) {
            String domainAlias = login.substring(0, slashPos);
            String domain = domainAliasesResolver.getDomainName(domainAlias).toUpperCase();
            String userName = login.substring(slashPos + 1);
            login = domain + "\\" + userName;
        } else {
            int atSignPos = login.indexOf("@");
            if (atSignPos >= 0) {
                String domainAlias = login.substring(atSignPos + 1);
                String domain = domainAliasesResolver.getDomainName(domainAlias).toUpperCase();
                String userName = login.substring(0, atSignPos);
                login = domain + "\\" + userName;
            }
        }
        return login;
    }

    @Override
    public boolean supports(Class<?> credentialsClass) {
        return webAuthConfig.getExternalAuthentication()
                && LoginPasswordCredentials.class.isAssignableFrom(credentialsClass);
    }

    @Override
    public int getOrder() {
        return HIGHEST_PLATFORM_PRECEDENCE + 20;
    }
}