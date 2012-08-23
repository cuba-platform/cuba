/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.portal.sys.security;

import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.portal.App;
import com.haulmont.cuba.portal.Connection;
import com.haulmont.cuba.portal.security.PortalSession;
import com.haulmont.cuba.security.app.UserSessionService;
import org.springframework.util.ClassUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Assigns current session's security association before each request goes to controller.
 * Clears security association after request completion.
 *
 * @author artamonov
 * @version $Id$
 */
public class SecurityContextHandlerInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {

        // filter resource requests
        if (ClassUtils.isAssignableValue(ResourceHttpRequestHandler.class, handler)) {
            return true;
        }

        HttpSession httpSession = request.getSession();
        Connection connection = (Connection) httpSession.getAttribute(Connection.NAME);
        if (connection == null || connection.getSession() == null || !connection.isConnected()) {
            connection = AppContext.getBean(Connection.NAME);
            connection.login(request.getLocale(), request.getRemoteAddr(), request.getHeader("User-Agent"));
            httpSession.setAttribute(Connection.NAME, connection);

            AppContext.setSecurityContext(new SecurityContext(connection.getSession()));
        } else {
            PortalSession session = connection.getSession();
            AppContext.setSecurityContext(new SecurityContext(session));
            // ping only authenticated sessions
            if (session != null && session.isAuthenticated()) {
                UserSessionService userSessionSource = AppContext.getBean(UserSessionService.NAME);
                userSessionSource.pingSession();
            }
        }

        App.registerConnection(connection, request, response);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {
        AppContext.setSecurityContext(null);
    }
}
