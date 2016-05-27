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

import org.aspectj.lang.ProceedingJoinPoint;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;

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