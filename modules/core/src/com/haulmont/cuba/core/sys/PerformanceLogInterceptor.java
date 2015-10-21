/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.sys;

import org.aspectj.lang.ProceedingJoinPoint;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;

/**
 * @author degtyarjov
 * @version $Id$
 */
public class PerformanceLogInterceptor {
    @SuppressWarnings("UnusedDeclaration")
    private Object aroundInvoke(ProceedingJoinPoint ctx) throws Throwable {
        StopWatch stopWatch = new Log4JStopWatch(ctx.getSignature().toShortString());
        try {
            stopWatch.start();
            Object res = ctx.proceed();
            return res;
        } finally {
            stopWatch.stop();
        }
    }
}
