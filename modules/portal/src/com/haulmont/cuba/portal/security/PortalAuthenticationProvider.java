/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.portal.security;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.PasswordEncryption;
import com.haulmont.cuba.portal.Connection;
import com.haulmont.cuba.portal.sys.security.RoleGrantedAuthority;
import com.haulmont.cuba.security.global.LoginException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
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

/**
 * @author artamonov
 */
public class PortalAuthenticationProvider implements AuthenticationProvider, Serializable {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;

            PortalSession session;

            try {
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
                HttpServletRequest request = attributes.getRequest();

                HttpSession httpSession = request.getSession();
                Connection connection = (Connection) httpSession.getAttribute(Connection.NAME);
                if (connection == null || connection.getSession() == null || !connection.isConnected()) {
                    connection = AppBeans.get(Connection.NAME);
                }

                PasswordEncryption passwordEncryption = AppBeans.get(PasswordEncryption.NAME);

                connection.login((String) token.getPrincipal(),
                        passwordEncryption.getPlainHash((String) token.getCredentials()),
                        request.getLocale(), request.getRemoteAddr(), request.getHeader("User-Agent"));

                httpSession.setAttribute(Connection.NAME, connection);

                session = connection.getSession();
            } catch (LoginException e) {
                throw new BadCredentialsException("error.login.User");
            }

            return new UsernamePasswordAuthenticationToken(session, session.getId(), getRoleUserAuthorities(session));
        }

        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }

    private List<GrantedAuthority> getRoleUserAuthorities(PortalSession portalSession) {
        final List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        if (!portalSession.isAuthenticated()) {
            return grantedAuthorities;
        } else {
            grantedAuthorities.add(new RoleGrantedAuthority());
        }
        return grantedAuthorities;
    }
}