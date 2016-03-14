/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.restapi;

import com.haulmont.cuba.core.global.UuidProvider;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.security.app.LoginService;
import com.haulmont.cuba.security.global.UserSession;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.UUID;

/**
 * @author chevelev
 * @version $Id$
 */
@Component(Authentication.NAME)
public class Authentication {

    public static final String NAME = "cuba_RestApiAuthentication";

    public static final String PERMISSION_NAME = "cuba.restApi.enabled";

    @Inject
    protected LoginService loginService;

    public boolean begin(String sessionId) {
        UUID uuid;
        try {
            uuid = UuidProvider.fromString(sessionId);
        } catch (Exception e) {
            return false;
        }

        UserSession session = loginService.getSession(uuid);
        if (session == null)
            return false;

        if (!session.isSpecificPermitted(PERMISSION_NAME))
            return false;

        AppContext.setSecurityContext(new SecurityContext(session));
        return true;
    }

    public void end() {
        AppContext.setSecurityContext(null);
    }
}