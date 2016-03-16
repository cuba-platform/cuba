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

package com.haulmont.cuba.web.auth;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;

/**
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