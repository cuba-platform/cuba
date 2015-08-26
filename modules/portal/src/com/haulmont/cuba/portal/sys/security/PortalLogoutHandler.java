/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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

/**
 * @author artamonov
 * @version $Id$
 */
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
