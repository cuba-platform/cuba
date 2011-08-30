/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 11.11.2008 18:29:38
 *
 * $Id$
 */
package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.SecurityProvider;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.sys.UserSessionManager;

import java.util.UUID;

public class SecurityProviderImpl extends SecurityProvider
{
    private UserSessionManager userSessionManager;

    public void setUserSessionManager(UserSessionManager userSessionManager) {
        this.userSessionManager = userSessionManager;
    }

    @Override
    protected boolean __checkCurrentUserSession() {
        SecurityContext securityContext = AppContext.getSecurityContext();
        if (securityContext == null)
            return false;

        UserSession userSession = userSessionManager.findSession(securityContext.getSessionId());
        return userSession != null;
    }

    @Override
    protected UserSession __currentUserSession() {
        SecurityContext securityContext = AppContext.getSecurityContext();
        if (securityContext == null)
            throw new SecurityException("No security context bound to the current thread");

        return userSessionManager.getSession(securityContext.getSessionId());
    }
}
