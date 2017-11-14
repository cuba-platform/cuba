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

package com.haulmont.cuba.web.auth;

import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.web.security.ldap.WebLdapConfig;
import org.apache.commons.lang.StringUtils;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.support.LdapUtils;

import javax.inject.Inject;
import javax.servlet.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @deprecated Use "cuba.web.ldap.enabled" application property instead.
 */
@Deprecated
public class LdapAuthProvider implements CubaAuthProvider {

    @Inject
    protected Messages messages;
    @Inject
    protected WebLdapConfig webLdapConfig;

    protected LdapContextSource ldapContextSource;

    protected LdapTemplate ldapTemplate;

    @Override
    public void authenticate(String login, String password, Locale messagesLocale) throws LoginException {
        if (!ldapTemplate.authenticate(LdapUtils.emptyLdapName(), buildPersonFilter(login), password)) {
            throw new LoginException(
                    messages.formatMessage(LdapAuthProvider.class, "LoginException.InvalidLoginOrPassword", messagesLocale, login)
            );
        }
    }

    protected String buildPersonFilter(String login) {
        AndFilter filter = new AndFilter();
        filter.and(new EqualsFilter("objectclass", "person"))
              .and(new EqualsFilter(webLdapConfig.getLdapUserLoginField(), login));
        return filter.encode();
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
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

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}