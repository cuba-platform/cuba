/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.portal.restapi;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.portal.security.PortalSession;
import com.haulmont.cuba.portal.sys.security.PortalSecurityContext;
import com.haulmont.cuba.portal.sys.security.PortalSessionFactory;
import com.haulmont.cuba.security.app.UserSessionService;
import com.haulmont.cuba.security.global.UserSession;

import java.util.UUID;

/**
 * @author chevelev
 * @version $Id$
 */
public class Authentication {
    public static Authentication me(String sessionId) {
        UserSession userSession = getSession(sessionId);
        if (userSession == null) {
            return null;
        }

        PortalSessionFactory portalSessionFactory = AppBeans.get(PortalSessionFactory.class);
        PortalSession portalSession = portalSessionFactory.createPortalSession(userSession, null);
        AppContext.setSecurityContext(new PortalSecurityContext(portalSession));

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
        return AppBeans.get(UserSessionService.class).getUserSession(sessionId);
    }
}