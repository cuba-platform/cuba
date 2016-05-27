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

import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.portal.Connection;
import com.haulmont.cuba.portal.security.PortalSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PortalLogoutHandler extends SimpleUrlLogoutSuccessHandler {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                Authentication authentication) throws IOException, ServletException {
        Connection connection = (Connection) request.getSession().getAttribute(Connection.NAME);
        try {
            if (connection != null) {
                SecurityContext portalSecurityContext = new PortalSecurityContext(connection.getSession());
                AppContext.setSecurityContext(portalSecurityContext);

                PortalSession session = connection.getSession();
                if (session != null && session.isAuthenticated())
                    connection.logout();
            }
        } catch (Exception e) {
            log.warn("Exception while logout", e);
        } finally {
            AppContext.setSecurityContext(null);
        }

        request.getSession().invalidate();

        super.onLogoutSuccess(request, response, authentication);
    }
}