/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.auth;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * @author artamonov
 * @version $Id$
 */
public class RequestContext {

    private static ThreadLocal<RequestContext> current = new ThreadLocal<>();

    private final HttpServletRequest request;

    private final HttpServletResponse response;

    private final long requestTimestamp;

    public RequestContext(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        this.requestTimestamp = new Date().getTime();
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public long getRequestTimestamp() {
        return requestTimestamp;
    }

    public HttpSession getSession() {
        return request != null ? request.getSession() : null;
    }

    public Cookie[] getCookies() {
        return request != null ? request.getCookies() : null;
    }

    public static void create(HttpServletRequest request, HttpServletResponse response) {
        current.set(new RequestContext(request, response));
    }

    public static void destroy() {
        current.set(null);
    }

    public static RequestContext get() {
        return current.get();
    }
}