/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.auth;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;

/**
 * @author krivopustov
 * @version $Id$
 */
public class ActiveDirectoryHelper {

    public static boolean useActiveDirectory() {
        WebAuthConfig config = AppBeans.get(Configuration.class).getConfig(WebAuthConfig.class);
        return config.getUseActiveDirectory();
    }

    /**
     * @return True if HTTP session support AD auth
     */
    public static boolean activeDirectorySupportedBySession() {
        RequestContext requestContext = RequestContext.get();
        return requestContext != null &&
                requestContext.getSession() != null &&
                getAuthProvider().authSupported(requestContext.getSession());
    }

    public static CubaAuthProvider getAuthProvider() {
        return AppBeans.get(CubaAuthProvider.NAME);
    }
}