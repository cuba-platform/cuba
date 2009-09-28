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

import com.vaadin.terminal.gwt.server.ApplicationServlet;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.core.app.ServerConfig;

import javax.servlet.http.HttpSession;
import java.io.BufferedWriter;
import java.io.IOException;

public class CubaApplicationServlet extends ApplicationServlet {
    private static final long serialVersionUID = -8701539520754293569L;

    @Override
    protected boolean isTestingMode() {
        return ConfigProvider.getConfig(ServerConfig.class).getTestMode();
    }

    @Override
    protected WebApplicationContext getApplicationContext(HttpSession session) {
        return CubaApplicationContext.getApplicationContext(session);
    }
}
