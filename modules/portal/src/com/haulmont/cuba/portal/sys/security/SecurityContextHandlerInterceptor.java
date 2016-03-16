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

package com.haulmont.cuba.portal.sys.security;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.portal.App;
import com.haulmont.cuba.portal.Connection;
import com.haulmont.cuba.portal.security.PortalSession;
import com.haulmont.cuba.security.app.UserSessionService;
import com.haulmont.cuba.security.global.NoUserSessionException;
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
 */
public class SecurityContextHandlerInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {

        // filter resource requests
        if (ClassUtils.isAssignableValue(ResourceHttpRequestHandler.class, handler)) {
            return true;
        }

        PortalSecurityContext portalSecurityContext;

        HttpSession httpSession = request.getSession();
        Connection connection = (Connection) httpSession.getAttribute(Connection.NAME);
        if (connection == null || connection.getSession() == null || !connection.isConnected()) {
            connection = AppBeans.get(Connection.NAME);
            connection.login(request.getLocale(), request.getRemoteAddr(), request.getHeader("User-Agent"));
            httpSession.setAttribute(Connection.NAME, connection);

            portalSecurityContext = new PortalSecurityContext(connection.getSession());
            AppContext.setSecurityContext(portalSecurityContext);
        } else {
            PortalSession session = connection.getSession();
            portalSecurityContext = new PortalSecurityContext(session);
            AppContext.setSecurityContext(portalSecurityContext);
            // ping only authenticated sessions
            if (session != null && session.isAuthenticated()) {
                UserSessionService userSessionSource = AppBeans.get(UserSessionService.NAME);
                try {
                    userSessionSource.getMessages();
                } catch (NoUserSessionException e) {
                    httpSession.invalidate();
                    response.sendRedirect(request.getRequestURI());
                    return false;
                }
            }
        }

        App app = new App(connection, request, response);
        portalSecurityContext.setPortalApp(app);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {
        AppContext.setSecurityContext(null);
    }
}
