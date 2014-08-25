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
        Configuration configuration = AppBeans.get(Configuration.NAME);
        WebAuthConfig config = configuration.getConfig(WebAuthConfig.class);
        return config.getUseActiveDirectory();
    }
}