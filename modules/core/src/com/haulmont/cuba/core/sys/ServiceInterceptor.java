/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 13.11.2008 14:13:23
 *
 * $Id$
 */
package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.global.RemoteException;
import com.haulmont.cuba.security.app.UserSessionsAPI;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;

public class ServiceInterceptor {

    private UserSessionsAPI userSessions;

    private Log log = LogFactory.getLog(getClass());

    public void setUserSessions(UserSessionsAPI userSessions) {
        this.userSessions = userSessions;
    }

    private Object aroundInvoke(ProceedingJoinPoint ctx) throws Throwable {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (int i = 2; i < stackTrace.length; i++) {
            StackTraceElement element = stackTrace[i];
            if (element.getClassName().equals(ServiceInterceptor.class.getName())) {
                log.error("Invoking " + ctx.getSignature() + " from another service");
                break;
            }
        }

        try {
            checkUserSession(ctx);

            Object res = ctx.proceed();
            return res;
        } catch (Throwable e) {
            log.error("ServiceInterceptor caught exception: ", e);
            // Propagate the special exception to avoid serialization errors on remote clients
            throw new RemoteException(e);
        }
    }

    private void checkUserSession(ProceedingJoinPoint ctx) {
        // Using UserSessionsAPI directly to make sure the session's "last used" timestamp is propagated to the cluster
        SecurityContext securityContext = AppContext.getSecurityContext();
        if (securityContext == null)
            throw new SecurityException("No security context bound to the current thread");

        UserSession userSession = userSessions.get(securityContext.getSessionId(), true);
        if (log.isTraceEnabled())
            log.trace("Invoking: " + ctx.getSignature() + ", session=" + userSession);
    }
}
