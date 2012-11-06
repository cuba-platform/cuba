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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author krivopustov
 * @version $Id$
 */
public class ActiveDirectoryHelper {
    public static boolean useActiveDirectory() {
        WebConfig config = AppBeans.get(Configuration.class).getConfig(WebConfig.class);
        return config.getUseActiveDirectory();
    }

    /**
     * @return True if HTTP session support AD auth
     */
    public static boolean activeDirectorySupportedBySession() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attr.getRequest();
        if (request == null)
            return false;
        HttpSession session = request.getSession();
        return session != null && getAuthProvider().authSupported(session);
    }

    public static CubaAuthProvider getAuthProvider() {
        return AppBeans.get(CubaAuthProvider.NAME);
    }
}