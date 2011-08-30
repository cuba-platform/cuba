/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 01.04.2009 15:24:21
 *
 * $Id$
 */
package com.haulmont.cuba.web.sys;

import com.haulmont.cuba.core.global.UserSessionProvider;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.security.app.UserSessionService;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.App;

public class WebUserSessionProvider extends UserSessionProvider
{
    protected UserSession __getUserSession() {
        if (App.isBound())
            return App.getInstance().getConnection().getSession();
        else {
            SecurityContext securityContext = AppContext.getSecurityContext();
            if (securityContext == null)
                throw new IllegalStateException("No security context bound to the current thread");

            if (securityContext.getSession() != null)
                return securityContext.getSession();
            else {
                UserSessionService uss = ServiceLocator.lookup(UserSessionService.NAME);
                return uss.getUserSession(securityContext.getSessionId());
            }
        }
    }
}
