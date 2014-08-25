/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.auth;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.security.global.LoginException;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.support.LdapUtils;

import javax.inject.Inject;
import javax.servlet.*;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * @author artamonov
 * @version $Id$
 */
public class LdapAuthProvider implements CubaAuthProvider {

    @Inject
    protected Messages messages;

    protected LdapContextSource ldapContextSource;

    protected LdapTemplate ldapTemplate;

    @Override
    public void authenticate(String login, String password, Locale messagesLocale) throws LoginException {
        if (!ldapTemplate.authenticate(LdapUtils.emptyLdapName(), buildPersonFilter(login), password)) {
            throw new LoginException(
                    String.format(messages.getMessage(LdapAuthProvider.class, "LoginException.InvalidLoginOrPassword", messagesLocale), login)
            );
        }
    }

    protected String buildPersonFilter(String login) {
        AndFilter filter = new AndFilter();
        filter.and(new EqualsFilter("objectclass", "person")).and(new EqualsFilter("sAMAccountName", login));
        return filter.encode();
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ldapContextSource = new LdapContextSource();

        Configuration configuration = AppBeans.get(Configuration.NAME);
        WebAuthConfig webAuthConfig = configuration.getConfig(WebAuthConfig.class);

        ldapContextSource.setBase(webAuthConfig.getLdapBase());
        List<String> ldapUrls = webAuthConfig.getLdapUrls();
        ldapContextSource.setUrls(ldapUrls.toArray(new String[ldapUrls.size()]));
        ldapContextSource.setUserDn(webAuthConfig.getLdapUser());
        ldapContextSource.setPassword(webAuthConfig.getLdapPassword());

        ldapContextSource.afterPropertiesSet();

        ldapTemplate = new LdapTemplate(ldapContextSource);
        ldapTemplate.setIgnorePartialResultException(true);
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