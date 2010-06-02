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
    @Override
    protected boolean __checkCurrentUserSession() {
        UUID sessionId = ServerSecurityUtils.getSessionId();
        if (sessionId == null)
            return false;

        UserSession userSession = UserSessionManager.getInstance().findSession(sessionId);
        return userSession != null;
    }

    @Override
    protected UserSession __currentUserSession() {
        UUID sessionId = ServerSecurityUtils.getSessionId();
        if (sessionId == null)
            throw new SecurityException("Session ID not found in security context");

        UserSession session = UserSessionManager.getInstance().getSession(sessionId);
        return session;
    }
}
