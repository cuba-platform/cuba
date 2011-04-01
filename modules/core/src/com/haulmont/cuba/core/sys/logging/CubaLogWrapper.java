/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 28.08.2009 15:44:33
 *
 * $Id$
 */
package com.haulmont.cuba.core.sys.logging;

import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.ServerSecurityUtils;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.sys.UserSessionManager;
import org.apache.commons.logging.Log;
import org.springframework.context.ApplicationContext;

import java.util.UUID;

public class CubaLogWrapper extends AbstractLogWrapper {

    public CubaLogWrapper(Log delegate) {
        super(delegate);
    }

    @Override
    protected String getUserInfo() {
        String logUserName = AppContext.getProperty("cuba.logUserName");
        if (logUserName == null || logUserName.equals("") || Boolean.valueOf(logUserName)) {
            UUID sessionId = ServerSecurityUtils.getSessionId();
            if (sessionId != null) {
                ApplicationContext context = AppContext.getApplicationContext();
                if (context != null) {
                    UserSessionManager usm = context.getBean(UserSessionManager.NAME, UserSessionManager.class);
                    UserSession session = usm.findSession(sessionId);
                    if (session != null) {
                        return session.getUser().getLogin();
                    }
                }
            }
        }
        return null;
    }
}
