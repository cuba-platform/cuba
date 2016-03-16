/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.haulmont.cuba.core.sys;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * Intercepts invocations of methods defined in <code>*MBean</code> interfaces.
 * <p/> Sets the thread context classloader to the webapp classloader and logs exceptions.
 *
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
