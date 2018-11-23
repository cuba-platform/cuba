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

package com.haulmont.restapi.auth;

import com.google.common.base.Strings;
import com.haulmont.cuba.client.sys.UsersRepository;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.security.app.TrustedClientService;
import com.haulmont.cuba.security.auth.AuthenticationDetails;
import com.haulmont.cuba.security.auth.AuthenticationService;
import com.haulmont.cuba.security.auth.LoginPasswordCredentials;
import com.haulmont.cuba.security.auth.TrustedClientCredentials;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.AccountLockedException;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.RestApiAccessDeniedException;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.restapi.common.RestAuthUtils;
import com.haulmont.restapi.config.RestApiConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CubaUserAuthenticationProvider implements AuthenticationProvider {

    protected static final String SESSION_ID_DETAILS_ATTRIBUTE = "sessionId";

    private static final Logger log = LoggerFactory.getLogger(CubaUserAuthenticationProvider.class);

    @Inject
    protected AuthenticationService authenticationService;

    @Inject
    protected PasswordEncryption passwordEncryption;

    @Inject
    protected Messages messages;

    @Inject
    protected UsersRepository usersRepository;

    @Inject
    protected TrustedClientService trustedClientService;

    @Inject
    protected RestAuthUtils restAuthUtils;

    @Inject
    protected RestApiConfig restApiConfig;

    @Inject
    protected GlobalConfig globalConfig;

    protected static final String MSG_PACK = "com.haulmont.restapi.auth";

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        String ipAddress = request.getRemoteAddr();

        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            if (!restApiConfig.getStandardAuthenticationEnabled()) {
                log.debug("Standard authentication is disabled. Property cuba.rest.standardAuthenticationEnabled is false");

                throw new InvalidGrantException("Authentication disabled");
            }

            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;

            String login = (String) token.getPrincipal();

            UserSession session;
            try {
                LoginPasswordCredentials credentials = new LoginPasswordCredentials(login, (String) token.getCredentials());
                credentials.setIpAddress(ipAddress);
                credentials.setClientType(ClientType.REST_API);
                credentials.setClientInfo(makeClientInfo(request.getHeader(HttpHeaders.USER_AGENT)));

                //if the locale value is explicitly passed in the Accept-Language header then set its value to the
                //credentials. Otherwise, the locale of the user should be used
                Locale locale = restAuthUtils.extractLocaleFromRequestHeader(request);
                if (locale != null) {
                    credentials.setLocale(locale);
                    credentials.setOverrideLocale(true);
                } else {
                    credentials.setOverrideLocale(false);
                }

                if (restApiConfig.getClientAuthentication()) {
                    session = loginClient(credentials).getSession();
                } else {
                    session = loginMiddleware(credentials).getSession();
                }

            } catch (AccountLockedException le) {
                log.info("Blocked user login attempt: login={}, ip={}", login, ipAddress);
                throw new LockedException("User temporarily blocked");
            } catch (RestApiAccessDeniedException ex) {
                log.info("User is not allowed to use the REST API {}", login);
                throw new BadCredentialsException("User is not allowed to use the REST API");
            } catch (LoginException e) {
                log.info("REST API authentication failed: {} {}", login, ipAddress);
                throw new BadCredentialsException("Bad credentials");
            }

            AppContext.setSecurityContext(new SecurityContext(session));

            UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(authentication.getPrincipal(),
                    authentication.getCredentials(), getRoleUserAuthorities(authentication));
            @SuppressWarnings("unchecked")
            Map<String, String> details = (Map<String, String>) authentication.getDetails();
            details.put(SESSION_ID_DETAILS_ATTRIBUTE, session.getId().toString());
            result.setDetails(details);
            return result;
        }

        return null;
    }

    protected AuthenticationDetails loginMiddleware(LoginPasswordCredentials credentials) throws LoginException {
        return authenticationService.login(credentials);
    }

    protected AuthenticationDetails loginClient(LoginPasswordCredentials credentials) {
        String login = credentials.getLogin();

        Locale credentialsLocale = credentials.getLocale() == null ?
                messages.getTools().getDefaultLocale() : credentials.getLocale();

        if (Strings.isNullOrEmpty(login)) {
            // empty login is not valid
            throw new LoginException(getInvalidCredentialsMessage(login, credentialsLocale));
        }

        UserSession systemSession = trustedClientService.getSystemSession(restApiConfig.getTrustedClientPassword());
        User user = AppContext.withSecurityContext(new SecurityContext(systemSession), () -> usersRepository.findUserByLogin(login));

        if (user == null) {
            throw new LoginException(getInvalidCredentialsMessage(login, credentialsLocale));
        }

        if (!passwordEncryption.checkPassword(user, credentials.getPassword())) {
            throw new LoginException(getInvalidCredentialsMessage(login, credentialsLocale));
        }

        return authenticationService.login(createTrustedCredentials(credentials));
    }

    protected TrustedClientCredentials createTrustedCredentials(LoginPasswordCredentials credentials) {
        TrustedClientCredentials tcCredentials = new TrustedClientCredentials(
                credentials.getLogin(),
                restApiConfig.getTrustedClientPassword(),
                credentials.getLocale(),
                credentials.getParams()
        );

        tcCredentials.setClientInfo(credentials.getClientInfo());
        tcCredentials.setClientType(ClientType.REST_API);
        tcCredentials.setIpAddress(credentials.getIpAddress());
        tcCredentials.setOverrideLocale(credentials.isOverrideLocale());
        tcCredentials.setSyncNewUserSessionReplication(credentials.isSyncNewUserSessionReplication());
        tcCredentials.setSessionAttributes(credentials.getSessionAttributes());

        return tcCredentials;
    }

    protected String getInvalidCredentialsMessage(String login, Locale locale) {
        return messages.formatMessage(MSG_PACK, "LoginException.InvalidLoginOrPassword", locale, login);
    }

    protected String makeClientInfo(String userAgent) {
        //noinspection UnnecessaryLocalVariable
        String serverInfo = String.format("REST API (%s:%s/%s) %s",
                globalConfig.getWebHostName(),
                globalConfig.getWebPort(),
                globalConfig.getWebContextName(),
                StringUtils.trimToEmpty(userAgent));

        return serverInfo;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }

    protected List<GrantedAuthority> getRoleUserAuthorities(Authentication authentication) {
        return new ArrayList<>();
    }
}