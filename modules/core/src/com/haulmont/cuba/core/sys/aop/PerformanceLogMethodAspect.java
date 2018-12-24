/*
 * Copyright (c) 2008-2018 Haulmont.
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
 */

package com.haulmont.cuba.core.sys.aop;

import com.haulmont.cuba.core.sys.ConditionalOnAppProperty;
import com.haulmont.cuba.core.sys.PerformanceLogInterceptor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Order(4)
@ConditionalOnAppProperty(property = "cuba.performanceLogDisabled", value = "false", defaultValue = "false")
public class PerformanceLogMethodAspect extends PerformanceLogInterceptor {

    @Pointcut("execution(@com.haulmont.cuba.core.sys.PerformanceLog * *(..))")
    public void executionPointcut() {
    }

    @Around("executionPointcut()")
    public Object advice(ProceedingJoinPoint ctx) throws Throwable {
        return aroundInvoke(ctx);
    }
}
