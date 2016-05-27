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

package com.haulmont.cuba.security.sys;

import com.haulmont.cuba.security.app.Authentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.aspectj.lang.ProceedingJoinPoint;

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