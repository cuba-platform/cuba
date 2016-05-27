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

package com.haulmont.cuba.portal;

import com.haulmont.cuba.client.sys.cache.ClientCacheManager;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.portal.sys.security.PortalSecurityContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

public class App {

    protected Connection connection;

    protected HttpServletRequest request;

    protected HttpServletResponse response;

    public App(Connection connection, HttpServletRequest request, HttpServletResponse response) {
        this.connection = connection;
        this.request = request;
        this.response = response;
        ClientCacheManager clientCacheManager = AppBeans.get(ClientCacheManager.NAME);
        clientCacheManager.initialize();
    }

    public static boolean isBound() {
        SecurityContext rawSecurityContext = AppContext.getSecurityContext();
        if (rawSecurityContext instanceof PortalSecurityContext) {
            PortalSecurityContext securityContext = (PortalSecurityContext) rawSecurityContext;
            return securityContext.getPortalApp() != null;
        } else {
            return false;
        }
    }

    public static App getInstance() {
        SecurityContext rawSecurityContext = AppContext.getSecurityContext();
        if (rawSecurityContext instanceof PortalSecurityContext) {
            PortalSecurityContext securityContext = (PortalSecurityContext) rawSecurityContext;
            return securityContext.getPortalApp();
        } else {
            return null;
        }
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