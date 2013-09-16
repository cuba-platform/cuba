/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.sys.logging;

import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.sys.UserSessionManager;
import org.apache.commons.logging.Log;
import org.springframework.context.ApplicationContext;

public class CubaLogWrapper extends AbstractLogWrapper {

    public CubaLogWrapper(Log delegate) {
        super(delegate);
    }

    @Override
    protected String getUserInfo() {
        String logUserName = AppContext.getProperty("cuba.logUserName");
        if (logUserName == null || logUserName.equals("") || Boolean.valueOf(logUserName)) {
            SecurityContext securityContext = AppContext.getSecurityContext();
            if (securityContext != null) {
                if (securityContext.getUser() != null)
                    return securityContext.getUser();
                else {
                    ApplicationContext context = AppContext.getApplicationContext();
                    if (context != null) {
                        UserSessionManager usm = context.getBean(UserSessionManager.NAME, UserSessionManager.class);
                        UserSession session = usm.findSession(securityContext.getSessionId());
                        if (session != null) {
                            return session.getUser().getLogin();
                        }
                    }
                }
            }
        }
        return null;
    }
}
