/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.sys.restapi;

import com.haulmont.cuba.core.Locator;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.sys.UserSessionManager;

import java.util.UUID;

/**
 * Author: Alexander Chevelev
 * Date: 25.04.2011
 * Time: 15:36:34
 */
public class Authentication {
    public static Authentication me(String sessionId) {
        UserSession userSession = getSession(sessionId);
        if (userSession == null) {
            return null;
        }
        AppContext.setSecurityContext(new SecurityContext(userSession));
        return new Authentication();
    }

    public void forget() {
        AppContext.setSecurityContext(null);
    }

    private static UserSession getSession(String sessionIdStr) {
        UUID sessionId;
        try {
            sessionId = UUID.fromString(sessionIdStr);
        } catch (Exception e) {
            return null;
        }
        return ((UserSessionManager) Locator.lookup(UserSessionManager.NAME)).getSession(sessionId);
    }

}
