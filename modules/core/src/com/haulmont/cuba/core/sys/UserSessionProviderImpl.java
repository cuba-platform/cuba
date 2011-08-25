/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.global.UserSessionProvider;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.sys.UserSessionManager;

import java.io.Serializable;
import java.util.UUID;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class UserSessionProviderImpl extends UserSessionProvider {

    private UserSessionManager userSessionManager;

    public void setUserSessionManager(UserSessionManager userSessionManager) {
        this.userSessionManager = userSessionManager;
    }

    @Override
    protected UserSession __getUserSession() {
        UUID sessionId = ServerSecurityUtils.getSessionId();
        if (sessionId == null)
            throw new SecurityException("Session ID not found in security context");

        return userSessionManager.getSession(sessionId);
    }

    @Override
    protected void __setSessionAttribute(String name, Serializable value) {
        __getUserSession().setAttribute(name, value);
    }

}
