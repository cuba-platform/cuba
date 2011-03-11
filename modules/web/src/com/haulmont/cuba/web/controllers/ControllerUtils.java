/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 *
 * Author: Nikolay Gorodnov
 * Created: 11.03.2011 12:27:03
 *
 * $Id$
 */
package com.haulmont.cuba.web.controllers;

import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.core.global.GlobalConfig;

import javax.servlet.http.HttpServletRequest;

public abstract class ControllerUtils {
    
    public static String getControllerURL(String mapping) {
        if (mapping == null) throw new IllegalArgumentException("Mapping cannot be null");
        GlobalConfig globalConfig = ConfigProvider.getConfig(GlobalConfig.class);

        StringBuilder sb = new StringBuilder();
        sb.append(getProtocol()).append("://")
                .append(globalConfig.getWebHostName());
        if (globalConfig.getWebPort() != null) {
            sb.append(":")
                    .append(globalConfig.getWebPort());
        }
        sb.append("/")
                .append(globalConfig.getWebContextName()).append(getContollerPrefix());
        if (!mapping.startsWith("/")) {
            sb.append("/");
        }
        sb.append(mapping);
        return sb.toString();
    }

    private static String getProtocol() {
        return "http";
    }

    public static String getContollerPrefix() {
        return "/dispatch";
    }

    public static String getControllerPath(HttpServletRequest request) {
        String path = request.getServletPath();
        if (path.startsWith(getContollerPrefix())) {
            path = path.substring(getContollerPrefix().length());
        }
        return path;
    }
}
