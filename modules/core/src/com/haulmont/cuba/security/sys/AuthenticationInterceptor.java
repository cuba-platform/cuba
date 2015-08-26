/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.security.sys;

import com.haulmont.cuba.security.app.Authentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * @author krivopustov
 * @version $Id$
 */
public class AuthenticationInterceptor {

    private Logger log = LoggerFactory.getLogger(getClass());

    private Authentication authentication;

    public void setAuthentication(Authentication authentication) {
        this.authentication = authentication;
    }

    private Object aroundInvoke(ProceedingJoinPoint ctx) throws Throwable {
        if (log.isTraceEnabled())
            log.trace("Authenticating: " + ctx.getSignature());

        try {
            authentication.begin();
            Object res = ctx.proceed();
            return res;
        } finally {
            authentication.end();
        }
    }
}
