/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.sys;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * Intercepts invocations of methods defined in <code>*MBean</code> interfaces.
 * <p/> Sets the thread context classloader to the webapp classloader and logs exceptions.
 *
 * @author krivopustov
 * @version $Id$
 */
public class MBeanInterceptor {

    private Logger log = LoggerFactory.getLogger(getClass());

    @SuppressWarnings("UnusedDeclaration")
    private Object aroundInvoke(ProceedingJoinPoint ctx) throws Throwable {
        Thread.currentThread().setContextClassLoader(getClass().getClassLoader());

        if (log.isTraceEnabled())
            log.trace("Invoking: " + ctx.getSignature());

        try {
            Object res = ctx.proceed();
            return res;
        } catch (Throwable e) {
            log.error("MBeanInterceptor caught exception: ", e);
            throw e;
        }
    }
}
