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

import com.haulmont.cuba.core.global.ClientType;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.PasswordEncryption;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.restapi.RestConfig;
import com.haulmont.cuba.security.auth.AuthenticationService;
import com.haulmont.cuba.security.auth.LoginPasswordCredentials;
import com.haulmont.cuba.security.global.AccountLockedException;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.RestApiAccessDeniedException;
import com.haulmont.cuba.security.global.UserSession;
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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CubaUserAuthenticationProvider implements AuthenticationProvider, Serializable {

    protected static final String SESSION_ID_DETAILS_ATTRIBUTE = "sessionId";

    private final Logger log = LoggerFactory.getLogger(CubaUserAuthenticationProvider.class);

    @Inject
    protected AuthenticationService authenticationService;

    @Inject
    protected PasswordEncryption passwordEncryption;

    @Inject
    protected Configuration configuration;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        String ipAddress = request.getRemoteAddr();

        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            RestConfig config = configuration.getConfig(RestConfig.class);
            if (!config.getStandardAuthenticationEnabled()) {
                log.debug("Standard authentication is disabled. Property cuba.rest.standardAuthenticationEnabled is false");

                throw new InvalidGrantException("Authentication disabled");
            }

            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;

            String login = (String) token.getPrincipal();

            UserSession session;
            try {
                String passwordHash = passwordEncryption.getPlainHash((String) token.getCredentials());

                LoginPasswordCredentials credentials = new LoginPasswordCredentials(login, passwordHash, request.getLocale());
                credentials.setClientType(ClientType.REST_API);
                credentials.setClientInfo("REST API");

                session = authenticationService.login(credentials).getSession();
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

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }

    protected List<GrantedAuthority> getRoleUserAuthorities(Authentication authentication) {
        return new ArrayList<>();
    }
}