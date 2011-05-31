/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.sys.restapi;

import com.haulmont.cuba.core.Locator;
import com.haulmont.cuba.core.sys.ServerSecurityUtils;
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

        ServerSecurityUtils.setSecurityAssociation(userSession.getUser().getLogin(), userSession.getId());
        return new Authentication();
    }

    public void forget() {
        ServerSecurityUtils.clearSecurityAssociation();
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
