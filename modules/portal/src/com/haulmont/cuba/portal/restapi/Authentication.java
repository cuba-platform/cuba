/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.portal.restapi;

import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.portal.security.PortalSession;
import com.haulmont.cuba.portal.sys.security.PortalSecurityContext;
import com.haulmont.cuba.portal.sys.security.PortalSessionFactory;
import com.haulmont.cuba.security.app.LoginService;
import com.haulmont.cuba.security.global.UserSession;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.util.UUID;

/**
 * @author chevelev
 * @version $Id$
 */
@ManagedBean(Authentication.NAME)
public class Authentication {

    public static final String NAME = "cuba_RestApiAuthentication";

    @Inject
    protected LoginService loginService;

    @Inject
    protected PortalSessionFactory portalSessionFactory;

    public boolean begin(String sessionId) {
        UUID uuid;
        try {
            uuid = UUID.fromString(sessionId);
        } catch (Exception e) {
            return false;
        }

        UserSession session = loginService.getSession(uuid);
        if (session == null)
            return false;

        PortalSession portalSession = portalSessionFactory.createPortalSession(session, null);
        AppContext.setSecurityContext(new PortalSecurityContext(portalSession));

        return true;
    }

    public void end() {
        AppContext.setSecurityContext(null);
    }
}