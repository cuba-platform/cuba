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

package com.haulmont.cuba.portal.security;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.portal.Connection;
import com.haulmont.cuba.portal.sys.security.RoleGrantedAuthority;
import com.haulmont.cuba.security.global.AccountLockedException;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserIpRestrictedException;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PortalAuthenticationProvider implements AuthenticationProvider, Serializable {

    private static final Logger log = LoggerFactory.getLogger(PortalAuthenticationProvider.class);

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;

            PortalSession session;

            String login = null;
            String ipAddress = null;
            try {
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
                HttpServletRequest request = attributes.getRequest();

                login = (String) token.getPrincipal();
                ipAddress = request.getRemoteAddr();

                HttpSession httpSession = request.getSession();
                Connection connection = (Connection) httpSession.getAttribute(Connection.NAME);
                if (connection == null || connection.getSession() == null || !connection.isConnected()) {
                    connection = AppBeans.get(Connection.NAME);
                }
                String password = (String) token.getCredentials();
                connection.login(login, password, request.getLocale(), ipAddress, request.getHeader("User-Agent"));

                httpSession.setAttribute(Connection.NAME, connection);

                session = connection.getSession();
            } catch (AccountLockedException e) {
                log.info("Blocked user login attempt: login={}, ip={}", login, ipAddress);
                throw new LockedException(e.getMessage());
            } catch (UserIpRestrictedException e) {
                log.info("Incorrect user IP: {} {} - {}", login, ipAddress);
                throw new BadCredentialsException(e.getMessage());
            } catch (LoginException e) {
                log.info("Authentication failed: {} {} - {}", login, ipAddress, e.getMessage());
                throw new BadCredentialsException(e.getMessage());
            }

            return new UsernamePasswordAuthenticationToken(session, session.getId(), getRoleUserAuthorities(session));
        }

        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }

    protected List<GrantedAuthority> getRoleUserAuthorities(PortalSession portalSession) {
        final List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        if (!portalSession.isAuthenticated()) {
            return grantedAuthorities;
        } else {
            grantedAuthorities.add(new RoleGrantedAuthority());
        }
        return grantedAuthorities;
    }
}