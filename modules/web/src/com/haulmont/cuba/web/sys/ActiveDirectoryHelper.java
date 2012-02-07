/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 10.01.2009 13:16:42
 *
 * $Id$
 */
package com.haulmont.cuba.web.sys;

import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.web.WebConfig;
import com.haulmont.cuba.web.sys.auth.CubaAuthProvider;

public class ActiveDirectoryHelper {
    public static boolean useActiveDirectory() {
        WebConfig config = ConfigProvider.getConfig(WebConfig.class);
        return config.getUseActiveDirectory();
    }

    public static CubaAuthProvider getAuthProvider() {
        return AppContext.getBean(CubaAuthProvider.NAME);
    }
}
