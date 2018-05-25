/*
 * Copyright (c) 2008-2018 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.idp.controllers;

import com.haulmont.cuba.core.global.ClientType;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.PasswordEncryption;
import com.haulmont.cuba.security.auth.LoginPasswordCredentials;
import com.haulmont.cuba.security.auth.TrustedClientCredentials;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.idp.IdpService;
import com.haulmont.idp.IdpAuthConfig;
import com.haulmont.idp.IdpAuthMode;
import com.haulmont.idp.IdpConfig;
import com.haulmont.idp.model.AuthRequest;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@Component("cuba_IdpAuthenticationManager")
public class IdpLoginManager implements InitializingBean {
    @Inject
    protected Logger log;

    @Inject
    protected IdpService idpService;
    @Inject
    protected IdpConfig idpConfig;

    @Inject
    protected IdpAuthConfig authenticationConfig;
    @Inject
    protected PasswordEncryption passwordEncryption;
    @Inject
    protected Messages messages;

    protected LdapContextSource ldapContextSource;
    protected LdapTemplate ldapTemplate;

    public IdpService.IdpLoginResult login(AuthRequest auth, Locale sessionLocale) throws LoginException {
        IdpAuthMode authenticationMode = authenticationConfig.getAuthenticationMode();
        List<String> standardAuthenticationUsers = authenticationConfig.getStandardAuthenticationUsers();

        if (standardAuthenticationUsers.contains(auth.getUsername())) {
            // user can only use STANDARD authentication
            authenticationMode = IdpAuthMode.STANDARD;
        }

        switch (authenticationMode) {
            case STANDARD: {
                LoginPasswordCredentials credentials = new LoginPasswordCredentials(
                        auth.getUsername(),
                        passwordEncryption.getPlainHash(auth.getPassword()),
                        sessionLocale);

                credentials.setClientType(ClientType.WEB);

                return idpService.login(credentials, Collections.emptyMap());
            }

            case LDAP: {
                if (!authenticateInLdap(auth)) {
                    throw new LoginException(
                            messages.formatMainMessage("LoginException.InvalidLoginOrPassword",
                                    sessionLocale, auth.getUsername()));
                }

                TrustedClientCredentials credentials = new TrustedClientCredentials(
                        auth.getUsername(),
                        idpConfig.getTrustedClientPassword(),
                        sessionLocale);

                credentials.setClientType(ClientType.WEB);

                return idpService.login(credentials, Collections.emptyMap());
            }

            default:
                log.error("Unsupported authentication mode {}", authenticationConfig.getAuthenticationMode());

                throw new LoginException(
                        messages.formatMainMessage("LoginException.InvalidLoginOrPassword",
                                sessionLocale, auth.getUsername()));
        }
    }

    protected boolean authenticateInLdap(AuthRequest auth) throws LoginException {
        String filter = buildPersonFilter(auth.getUsername());

        return ldapTemplate.authenticate(LdapUtils.emptyLdapName(), filter, auth.getPassword());
    }

    @Override
    public void afterPropertiesSet() {
        if (authenticationConfig.getAuthenticationMode() == IdpAuthMode.LDAP) {
            checkRequiredConfigProperties(authenticationConfig);

            ldapContextSource = createLdapContextSource(authenticationConfig);
            ldapTemplate = createLdapTemplate(ldapContextSource);
        }
    }

    protected String buildPersonFilter(String login) {
        AndFilter filter = new AndFilter();
        filter.and(new EqualsFilter("objectclass", "person"))
                .and(new EqualsFilter(authenticationConfig.getLdapUserLoginField(), login));
        return filter.encode();
    }

    protected void checkRequiredConfigProperties(IdpAuthConfig idpAuthConfig) {
        List<String> missingProperties = new ArrayList<>();
        if (StringUtils.isBlank(idpAuthConfig.getLdapBase())) {
            missingProperties.add("cuba.idp.ldap.base");
        }
        if (idpAuthConfig.getLdapUrls().isEmpty()) {
            missingProperties.add("cuba.idp.ldap.urls");
        }
        if (StringUtils.isBlank(idpAuthConfig.getLdapUser())) {
            missingProperties.add("cuba.idp.ldap.user");
        }
        if (StringUtils.isBlank(idpAuthConfig.getLdapPassword())) {
            missingProperties.add("cuba.idp.ldap.password");
        }

        if (!missingProperties.isEmpty()) {
            throw new IllegalStateException("Please configure required application properties for LDAP integration: \n" +
                    StringUtils.join(missingProperties, "\n"));
        }
    }

    protected LdapTemplate createLdapTemplate(LdapContextSource ldapContextSource) {
        LdapTemplate ldapTemplate = new LdapTemplate(ldapContextSource);
        ldapTemplate.setIgnorePartialResultException(true);

        return ldapTemplate;
    }

    protected LdapContextSource createLdapContextSource(IdpAuthConfig idpAuthConfig) {
        LdapContextSource ldapContextSource = new LdapContextSource();

        ldapContextSource.setBase(idpAuthConfig.getLdapBase());
        List<String> ldapUrls = idpAuthConfig.getLdapUrls();
        ldapContextSource.setUrls(ldapUrls.toArray(new String[ldapUrls.size()]));
        ldapContextSource.setUserDn(idpAuthConfig.getLdapUser());
        ldapContextSource.setPassword(idpAuthConfig.getLdapPassword());

        ldapContextSource.afterPropertiesSet();

        return ldapContextSource;
    }
}