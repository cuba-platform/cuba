/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.cuba.core.app.ManagementBean;
import com.haulmont.cuba.security.global.LoginException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;

/**
 * @author artamonov
 * @version $Id$
 */
@SuppressWarnings("UnusedDeclaration")
public class AuthenticatedMBeanInterceptor {
    private Log log = LogFactory.getLog(getClass());

    private Object beforeInvoke(ProceedingJoinPoint ctx) throws Throwable {
        Thread.currentThread().setContextClassLoader(getClass().getClassLoader());

        if (log.isTraceEnabled())
            log.trace("Invoking: " + ctx.getSignature());

        try {
            if (ctx.getTarget() instanceof ManagementBean) {
                ManagementBean target = (ManagementBean) ctx.getTarget();
                Method loginMethod = ReflectionHelper.findMethod(ManagementBean.class, "loginOnce");
                loginMethod.setAccessible(true);
                loginMethod.invoke(target);
            }

            Object res = ctx.proceed();
            return res;
        } catch (LoginException e) {
            log.error("Authenticated login failed:", e);
            throw e;
        } catch (Throwable e) {
            log.error("MBeanInterceptor caught exception: ", e);
            throw e;
        }
    }
}