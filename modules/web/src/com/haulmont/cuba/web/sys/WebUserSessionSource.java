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

package com.haulmont.cuba.web.sys;

import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.core.sys.AbstractUserSessionSource;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.security.app.UserSessionService;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.Connection;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 */
@Component(UserSessionSource.NAME)
public class WebUserSessionSource extends AbstractUserSessionSource {

    @Inject
    private UserSessionService userSessionService;

    @Override
    public boolean checkCurrentUserSession() {
        if (App.isBound()) {
            App app = App.getInstance();
            Connection connection = app.getConnection();
            return connection.isConnected() && connection.getSession() != null;
        } else {
            SecurityContext securityContext = AppContext.getSecurityContext();
            if (securityContext == null) {
                return false;
            }

            if (securityContext.getSession() != null) {
                return true;
            } else {
                try {
                    userSessionService.getUserSession(securityContext.getSessionId());
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }
        }
    }

    @Override
    public UserSession getUserSession() {
        UserSession session;
        if (App.isBound()) {
            session = App.getInstance().getConnection().getSession();
        } else {
            SecurityContext securityContext = AppContext.getSecurityContextNN();
            if (securityContext.getSession() != null) {
                session = securityContext.getSession();
            } else {
                session = userSessionService.getUserSession(securityContext.getSessionId());
            }
        }
        if (session == null) {
            throw new IllegalStateException("No user session");
        }
        return session;
    }
}