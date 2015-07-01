/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.sys;

import com.haulmont.cuba.core.global.ClientType;
import com.haulmont.cuba.core.sys.AbstractWebAppContextLoader;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.gui.AppConfig;

/**
 * {@link AppContext} loader of the web client application.
 *
 * @author krivopustov
 * @version $Id$
 */
public class WebAppContextLoader extends AbstractWebAppContextLoader {

    @Override
    protected void beforeInitAppContext() {
        super.beforeInitAppContext();

        AppContext.setProperty(AppConfig.CLIENT_TYPE_PROP, ClientType.WEB.toString());
    }
}