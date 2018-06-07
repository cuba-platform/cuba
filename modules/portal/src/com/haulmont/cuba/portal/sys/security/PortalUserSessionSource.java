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

import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.core.sys.AbstractUserSessionSource;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.portal.App;
import com.haulmont.cuba.portal.Connection;
import com.haulmont.cuba.security.app.UserSessionService;
import com.haulmont.cuba.security.global.UserSession;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Component(UserSessionSource.NAME)
public class PortalUserSessionSource extends AbstractUserSessionSource {

    public static final String REQUEST_ATTR = "CUBA_USER_SESSION";

    @Inject
    protected UserSessionService userSessionService;

    @Inject
    protected PortalSessionFactory portalSessionFactory;

    @Override
    public boolean checkCurrentUserSession() {
        if (App.isBound()) {
            Connection connection = App.getInstance().getConnection();
            return connection.isConnected() && connection.getSession() != null;
        } else {
            SecurityContext securityContext = AppContext.getSecurityContext();
            if (securityContext == null)
                return false;

            if (securityContext.getSession() != null)
                return true;
            else if (AppContext.isStarted()) {
                try {
                    getUserSessionFromMiddleware(securityContext.getSessionId());
                    return true;
                } catch (Exception e) {
                    return false;
                }
            } else
                return false;
        }
    }

    @Override
    public UserSession getUserSession() {
        if (App.isBound())
            return App.getInstance().getConnection().getSession();
        else {
            SecurityContext securityContext = AppContext.getSecurityContextNN();
            if (securityContext.getSession() != null)
                return securityContext.getSession();
            else {
                UserSession userSession = getUserSessionFromMiddleware(securityContext.getSessionId());
                return portalSessionFactory.createPortalSession(userSession, null);
            }
        }
    }

    protected UserSession getUserSessionFromMiddleware(UUID sessionId) {
        UserSession userSession = null;

        HttpServletRequest request = null;
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            request = ((ServletRequestAttributes) requestAttributes).getRequest();
        }

        if (request != null) {
            userSession = (UserSession) request.getAttribute(REQUEST_ATTR);
        }
        if (userSession != null) {
            return userSession;
        }
        userSession = userSessionService.getUserSession(sessionId);
        if (request != null) {
            request.setAttribute(REQUEST_ATTR, userSession);
        }
        return userSession;
    }
}