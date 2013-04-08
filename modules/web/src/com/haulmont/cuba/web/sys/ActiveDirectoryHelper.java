/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.web.sys;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.web.WebConfig;
import com.haulmont.cuba.web.sys.auth.CubaAuthProvider;

import javax.servlet.http.HttpSession;

/**
 * @author krivopustov
 * @version $Id$
 */
public class ActiveDirectoryHelper {

    private final static ThreadLocal<HttpSession> currentSession = new ThreadLocal<>();

    public static boolean useActiveDirectory() {
        WebConfig config = AppBeans.get(Configuration.class).getConfig(WebConfig.class);
        return config.getUseActiveDirectory();
    }

    /**
     * @return True if HTTP session support AD auth
     */
    public static boolean activeDirectorySupportedBySession() {
        RequestContext requestContext = RequestContext.get();
        return requestContext != null &&
                requestContext.getSession() != null &&
                getAuthProvider().authSupported(currentSession.get());
    }

    public static CubaAuthProvider getAuthProvider() {
        return AppBeans.get(CubaAuthProvider.NAME);
    }
}