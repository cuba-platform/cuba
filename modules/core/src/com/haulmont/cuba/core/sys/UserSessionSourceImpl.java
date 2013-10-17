/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.sys.UserSessionManager;

import javax.annotation.ManagedBean;
import javax.inject.Inject;

/**
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean(UserSessionSource.NAME)
public class UserSessionSourceImpl extends AbstractUserSessionSource {

    @Inject
    private UserSessionManager userSessionManager;

    @Override
    public boolean checkCurrentUserSession() {
        SecurityContext securityContext = AppContext.getSecurityContext();
        return securityContext != null && userSessionManager.findSession(securityContext.getSessionId()) != null;
    }

    @Override
    public UserSession getUserSession() {
        SecurityContext securityContext = AppContext.getSecurityContext();
        if (securityContext == null)
            throw new SecurityException("No security context bound to the current thread");

        return userSessionManager.getSession(securityContext.getSessionId());
    }
}
