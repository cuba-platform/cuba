/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.portal;

import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.portal.sys.security.PortalSecurityContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * @author artamonov
 * @version $Id$
 */
public class App {

    protected Connection connection;

    protected HttpServletRequest request;

    protected HttpServletResponse response;

    public App(Connection connection, HttpServletRequest request, HttpServletResponse response) {
        this.connection = connection;
        this.request = request;
        this.response = response;
    }

    public static boolean isBound() {
        PortalSecurityContext securityContext = (PortalSecurityContext) AppContext.getSecurityContext();
        return securityContext != null && securityContext.getPortalApp() != null;
    }

    public static App getInstance() {
        PortalSecurityContext securityContext = (PortalSecurityContext) AppContext.getSecurityContext();
        return securityContext != null ? securityContext.getPortalApp() : null;
    }

    public Connection getConnection() {
        return connection;
    }

    public String getIpAddress() {
        return request.getRemoteAddr();
    }

    public Locale getLocale() {
        return request.getLocale();
    }

    public String getClientInfo() {
        return request.getHeader("User-Agent");
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }
}
