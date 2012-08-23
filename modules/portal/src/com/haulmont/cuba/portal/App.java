/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.portal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * @author artamonov
 * @version $Id$
 */
public class App {

    protected static ThreadLocal<App> currentApp = new ThreadLocal<>();

    protected Connection connection;

    protected HttpServletRequest request;

    protected HttpServletResponse response;

    protected App(Connection connection) {
        this.connection = connection;
    }

    public static void registerConnection(Connection connection,
                                          HttpServletRequest request, HttpServletResponse response) {
        App app = new App(connection);
        app.request = request;
        app.response = response;
        currentApp.set(app);
    }

    public static boolean isBound() {
        return currentApp.get() != null;
    }

    public static App getInstance() {
        return currentApp.get();
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
