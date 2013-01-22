/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
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
        AppContext.setProperty(AppConfig.CLIENT_TYPE_PROP, ClientType.WEB.toString());
    }
}
