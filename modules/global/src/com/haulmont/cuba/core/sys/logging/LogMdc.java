/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.logging;

import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.core.sys.UserSessionFinder;
import com.haulmont.cuba.security.global.UserSession;
import org.slf4j.MDC;
import org.springframework.context.ApplicationContext;

/**
 * @author Konstantin Krivopustov
 * @version $Id$
 */
public class LogMdc {

    public static final String USER = "cubaUser";
    public static final String APPLICATION = "cubaApp";

    public static void setup(SecurityContext securityContext) {
        String userProp = AppContext.getProperty("cuba.logUserName");
        if (userProp == null || Boolean.valueOf(userProp)) {
            if (securityContext != null) {
                String user = securityContext.getUser();
                if (user == null) {
                    UserSession session = securityContext.getSession();
                    if (session != null)
                        user = session.getUser().getLogin();
                    else if (securityContext.getSessionId() != null) {
                        ApplicationContext applicationContext = AppContext.getApplicationContext();
                        if (applicationContext.containsBean("cuba_UserSessionManager")) {
                            UserSessionFinder sessionFinder = (UserSessionFinder) applicationContext.getBean("cuba_UserSessionManager");
                            session = sessionFinder.findSession(securityContext.getSessionId());
                            if (session != null) {
                                user = session.getUser().getLogin();
                            }
                        }
                    }
                }
                if (user != null) {
                    MDC.put(USER, "/" + user);
                }
            } else {
                MDC.remove(USER);
            }
        }

        String applicationProp = AppContext.getProperty("cuba.logAppName");
        if (applicationProp == null || Boolean.valueOf(applicationProp)) {
            if (securityContext != null) {
                MDC.put(APPLICATION, "/" + AppContext.getProperty("cuba.webContextName"));
            } else {
                MDC.remove(APPLICATION);
            }
        }
    }
}
