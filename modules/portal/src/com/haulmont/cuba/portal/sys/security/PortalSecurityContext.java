/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.portal.sys.security;

import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.portal.App;
import com.haulmont.cuba.security.global.UserSession;

/**
 * @author artamonov
 * @version $Id$
 */
public class PortalSecurityContext extends SecurityContext {

    private App portalApp;

    public PortalSecurityContext(UserSession session) {
        super(session);
    }

    public void setPortalApp(App portalApp) {
        this.portalApp = portalApp;
    }

    public App getPortalApp() {
        return portalApp;
    }
}
