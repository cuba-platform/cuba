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
package com.haulmont.cuba.web.controllers;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.security.app.TrustedClientService;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.auth.WebAuthConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.UUID;

public final class ControllerUtils {
    private static final String DISPATCHER = "dispatch";

    private static final Logger log = LoggerFactory.getLogger(ControllerUtils.class);

    private ControllerUtils() {
    }

    /**
     * The URL string that is returned will have '/' in the end
     */
    public static String getLocationWithoutParams() {
        URI location = AppUI.getCurrent().getPage().getLocation();
        return getLocationWithoutParams(location);
    }

    /**
     * The URL string that is returned will have '/' in the end
     */
    public static String getLocationWithoutParams(URI location) {
        try {
            StringBuilder baseUrl = new StringBuilder(location.toURL().toExternalForm());
            if (location.getQuery() != null) {
                baseUrl.delete(baseUrl.indexOf("?" + location.getQuery()), baseUrl.length());
            } else if (location.getFragment() != null) {
                baseUrl.delete(baseUrl.indexOf("#" + location.getFragment()), baseUrl.length());
            }
            String baseUrlString = baseUrl.toString();
            return baseUrlString.endsWith("/") ? baseUrlString : baseUrlString + "/";
        } catch (MalformedURLException e) {
            throw new RuntimeException("Unable to get location without params", e);
        }
    }

    public static String getWebControllerURL(String mapping) {
        if (mapping == null) throw new IllegalArgumentException("Mapping cannot be null");
        String baseUrl = getLocationWithoutParams();

        StringBuilder url = new StringBuilder(baseUrl).append(getDispatcher());
        if (!mapping.startsWith("/")) {
            url.append("/");
        }
        url.append(mapping);
        return url.toString();
    }

    public static String getControllerURL(String mapping) {
        if (mapping == null) throw new IllegalArgumentException("Mapping cannot be null");
        Configuration configuration = AppBeans.get(Configuration.NAME);
        GlobalConfig globalConfig = configuration.getConfig(GlobalConfig.class);

        StringBuilder sb = new StringBuilder(globalConfig.getWebAppUrl()).append(getControllerPrefix());
        if (!mapping.startsWith("/")) {
            sb.append("/");
        }
        sb.append(mapping);
        return sb.toString();
    }

    public static String getDispatcher() {
        return DISPATCHER;
    }

    public static String getControllerPrefix() {
        return "/" + DISPATCHER;
    }

    public static String getControllerPath(HttpServletRequest request) {
        String path = request.getServletPath();
        if (path.startsWith(getControllerPrefix())) {
            path = path.substring(getControllerPrefix().length());
        }
        return path;
    }

    public static UserSession getUserSession(HttpServletRequest req) {
        String s = req.getParameter("s");
        if (s != null) {
            try {
                UUID id = UUID.fromString(s);

                WebAuthConfig webAuthConfig = AppBeans.get(Configuration.class).getConfig(WebAuthConfig.class);

                TrustedClientService trustedClientService = AppBeans.get(TrustedClientService.NAME);
                UserSession session = trustedClientService.findSession(webAuthConfig.getTrustedClientPassword(), id);

                if (session != null) {
                    req.getSession().setAttribute(App.USER_SESSION_ATTR, session);
                    return session;
                } else {
                    return null;
                }
            } catch (Exception e) {
                log.warn("Unable to get session from Login Service", e);
                return null;
            }
        } else {
            //noinspection UnnecessaryLocalVariable
            UserSession userSession = (UserSession) req.getSession().getAttribute(App.USER_SESSION_ATTR);
            return userSession;
        }
    }
}