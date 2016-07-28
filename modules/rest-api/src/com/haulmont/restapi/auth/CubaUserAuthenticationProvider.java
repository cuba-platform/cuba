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


import com.haulmont.cuba.core.global.PasswordEncryption;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.security.app.LoginService;
import com.haulmont.cuba.security.global.LoginException;
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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CubaUserAuthenticationProvider implements AuthenticationProvider, Serializable {

    protected Logger log = LoggerFactory.getLogger(getClass());

    @Inject
    protected LoginService loginService;

    @Inject
    protected PasswordEncryption passwordEncryption;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;

            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = attributes.getRequest();

            String login = (String) token.getPrincipal();
            String ipAddress = request.getRemoteAddr();

            checkBruteForceProtection(login, ipAddress);

            try {
                UserSession session = loginService.login(login, passwordEncryption.getPlainHash((String) token.getCredentials()), request.getLocale());
                AppContext.setSecurityContext(new SecurityContext(session));
                UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(authentication.getPrincipal(),
                        authentication.getCredentials(), getRoleUserAuthorities());
                Map<String, String> details = (Map<String, String>) authentication.getDetails();
                details.put("sessionId", session.getId().toString());
                result.setDetails(details);
                return result;
            } catch (LoginException e) {
                throw new BadCredentialsException("Bad credentials");
            }
        }

        return null;
    }

    private void checkBruteForceProtection(String login, String ipAddress) {
        if (loginService.isBruteForceProtectionEnabled()) {
            if (loginService.loginAttemptsLeft(login, ipAddress) <= 0) {
                log.info("Blocked user login attempt: login={}, ip={}", login, ipAddress);
                throw new LockedException("User temporarily blocked");
            }
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }

    private List<GrantedAuthority> getRoleUserAuthorities() {
        return new ArrayList<>();
    }
}