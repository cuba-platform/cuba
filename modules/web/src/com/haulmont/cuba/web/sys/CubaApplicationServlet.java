/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 27.08.2009 18:39:09
 *
 * $Id$
 */
package com.haulmont.cuba.web.sys;

import com.itmill.toolkit.terminal.gwt.server.ApplicationServlet;
import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.core.app.ServerConfig;

public class CubaApplicationServlet extends ApplicationServlet {
    @Override
    protected boolean isTestingMode() {
        return ConfigProvider.getConfig(ServerConfig.class).getTestMode();
    }
}
