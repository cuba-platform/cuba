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

package com.haulmont.cuba.web.security.ldap;

import com.google.common.collect.ImmutableMap;
import com.haulmont.cuba.core.global.ClientType;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.sys.ConditionalOnAppProperty;
import com.haulmont.cuba.security.auth.*;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.web.auth.WebAuthConfig;
import com.haulmont.cuba.web.security.LoginProvider;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.*;

import static com.haulmont.cuba.web.security.ExternalUserCredentials.EXTERNAL_AUTH_USER_SESSION_ATTRIBUTE;

/**
 * Login provider that uses LDAP as authentication service.
 *
 * @see WebLdapConfig
 */
@ConditionalOnAppProperty(property = "cuba.web.ldap.enabled", value = "true")
@Component("cuba_LdapLoginProvider")
public class LdapLoginProvider implements LoginProvider, Ordered {

    private static final Logger log = LoggerFactory.getLogger(LdapLoginProvider.class);

    @Inject
    protected AuthenticationService authenticationService;
    @Inject
    protected WebLdapConfig webLdapConfig;
    @Inject
    protected WebAuthConfig webAuthConfig;

    @Inject
    protected Messages messages;

    protected LdapContextSource ldapContextSource;

    protected LdapTemplate ldapTemplate;

    @Nullable
    @Override
    public AuthenticationDetails login(Credentials credentials) throws LoginException {
        LoginPasswordCredentials loginPasswordCredentials = (LoginPasswordCredentials) credentials;

        if (webAuthConfig.getStandardAuthenticationUsers().contains(loginPasswordCredentials.getLogin())) {
            log.debug("User {} is not allowed to use external login");
            return null;
        }

        if (!authenticateInLdap(loginPasswordCredentials)) {
            Locale locale = loginPasswordCredentials.getLocale();
            if (locale == null) {
                locale = messages.getTools().getDefaultLocale();
            }

            throw new LoginException(
                    messages.formatMessage(LdapLoginProvider.class, "LoginException.InvalidLoginOrPassword",
                            locale, loginPasswordCredentials.getLogin())
            );
        }

        TrustedClientCredentials tcCredentials = new TrustedClientCredentials(
                loginPasswordCredentials.getLogin(),
                webAuthConfig.getTrustedClientPassword(),
                loginPasswordCredentials.getLocale(),
                loginPasswordCredentials.getParams()
        );

        tcCredentials.setClientInfo(loginPasswordCredentials.getClientInfo());
        tcCredentials.setClientType(ClientType.WEB);
        tcCredentials.setIpAddress(loginPasswordCredentials.getIpAddress());
        tcCredentials.setOverrideLocale(loginPasswordCredentials.isOverrideLocale());
        tcCredentials.setSyncNewUserSessionReplication(loginPasswordCredentials.isSyncNewUserSessionReplication());
        tcCredentials.setSecurityScope(webAuthConfig.getSecurityScope());

        Map<String, Serializable> sessionAttributes = ((AbstractClientCredentials) credentials).getSessionAttributes();
        Map<String, Serializable> targetSessionAttributes;
        if (sessionAttributes != null
                && !sessionAttributes.isEmpty()) {
            targetSessionAttributes = new HashMap<>(sessionAttributes);
            targetSessionAttributes.put(EXTERNAL_AUTH_USER_SESSION_ATTRIBUTE, true);
        } else {
            targetSessionAttributes = ImmutableMap.of(EXTERNAL_AUTH_USER_SESSION_ATTRIBUTE, true);
        }

        tcCredentials.setSessionAttributes(targetSessionAttributes);

        return loginMiddleware(tcCredentials);
    }

    protected AuthenticationDetails loginMiddleware(Credentials credentials) throws LoginException {
        return authenticationService.login(credentials);
    }

    protected boolean authenticateInLdap(LoginPasswordCredentials credentials) throws LoginException {
        String login = credentials.getLogin();
        String password = credentials.getPassword();

        return ldapTemplate.authenticate(LdapUtils.emptyLdapName(), buildPersonFilter(login), password);
    }

    @Override
    public boolean supports(Class<?> credentialsClass) {
        return webLdapConfig.getLdapEnabled()
                && LoginPasswordCredentials.class.isAssignableFrom(credentialsClass);
    }

    @Override
    public int getOrder() {
        return HIGHEST_PLATFORM_PRECEDENCE + 40;
    }

    protected String buildPersonFilter(String login) {
        AndFilter filter = new AndFilter();
        filter.and(new EqualsFilter("objectclass", "person"))
                .and(new EqualsFilter(webLdapConfig.getLdapUserLoginField(), login));
        return filter.encode();
    }

    protected void checkRequiredConfigProperties(WebLdapConfig webLdapConfig) {
        List<String> missingProperties = new ArrayList<>();
        if (StringUtils.isBlank(webLdapConfig.getLdapBase())) {
            missingProperties.add("cuba.web.ldap.base");
        }
        if (webLdapConfig.getLdapUrls().isEmpty()) {
            missingProperties.add("cuba.web.ldap.urls");
        }
        if (StringUtils.isBlank(webLdapConfig.getLdapUser())) {
            missingProperties.add("cuba.web.ldap.user");
        }
        if (StringUtils.isBlank(webLdapConfig.getLdapPassword())) {
            missingProperties.add("cuba.web.ldap.password");
        }

        if (!missingProperties.isEmpty()) {
            throw new IllegalStateException("Please configure required application properties for LDAP integration: \n" +
                    StringUtils.join(missingProperties, "\n"));
        }
    }

    @PostConstruct
    protected void init() {
        if (webLdapConfig.getLdapEnabled()) {
            ldapContextSource = new LdapContextSource();

            checkRequiredConfigProperties(webLdapConfig);

            ldapContextSource.setBase(webLdapConfig.getLdapBase());
            List<String> ldapUrls = webLdapConfig.getLdapUrls();
            ldapContextSource.setUrls(ldapUrls.toArray(new String[ldapUrls.size()]));
            ldapContextSource.setUserDn(webLdapConfig.getLdapUser());
            ldapContextSource.setPassword(webLdapConfig.getLdapPassword());

            ldapContextSource.afterPropertiesSet();

            ldapTemplate = new LdapTemplate(ldapContextSource);
            ldapTemplate.setIgnorePartialResultException(true);
        }
    }
}