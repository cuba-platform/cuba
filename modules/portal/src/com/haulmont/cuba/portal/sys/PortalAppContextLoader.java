/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.portal.sys;

import com.haulmont.cuba.core.global.ClientType;
import com.haulmont.cuba.core.sys.AbstractWebAppContextLoader;
import com.haulmont.cuba.core.sys.AppContext;

/**
 * {@link AppContext} loader of the web portal client application.
 *
 * @author artamonov
 * @version $Id$
 */
public class PortalAppContextLoader extends AbstractWebAppContextLoader {

    @Override
    protected void beforeInitAppContext() {
        super.beforeInitAppContext();

        AppContext.setProperty("cuba.clientType", ClientType.PORTAL.toString());
    }
}
