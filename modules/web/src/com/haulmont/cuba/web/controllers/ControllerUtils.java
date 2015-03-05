/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.controllers;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.security.app.LoginService;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.AppUI;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.UUID;

/**
 * @author gorodnov
 * @version $Id$
 */
public final class ControllerUtils {
    private static final String DISPATCHER = "dispatch";

    private static final Log log = LogFactory.getLog(ControllerUtils.class);

    private ControllerUtils() {
    }

    public static String getLocationWithoutParams() {
        URI location = AppUI.getCurrent().getPage().getLocation();
        return getLocationWithoutParams(location);
    }

    public static String getLocationWithoutParams(URI location) {
        try {
            StringBuilder baseUrl = new StringBuilder(location.toURL().toExternalForm());
            if (location.getQuery() != null) {
                baseUrl.delete(baseUrl.indexOf("?" + location.getQuery()), baseUrl.length());
            }
            if (location.getFragment() != null) {
                baseUrl.delete(baseUrl.indexOf("#" + location.getFragment()), baseUrl.length());
            }
            return baseUrl.toString();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
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
                LoginService service = AppBeans.get(LoginService.NAME);
                UserSession session = service.getSession(id);
                if (session != null) {
                    req.getSession().setAttribute(App.USER_SESSION_ATTR, session);
                    return session;
                } else {
                    return null;
                }
            } catch (Exception e) {
                log.warn(e);
                return null;
            }
        } else {
            UserSession userSession = (UserSession) req.getSession().getAttribute(App.USER_SESSION_ATTR);
            return userSession;
        }
    }
}