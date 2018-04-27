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

package com.haulmont.cuba.security.sys;

import com.haulmont.cuba.core.sys.remoting.RemoteClientInfo;
import com.haulmont.cuba.security.global.TrustedAccessRequiredException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;

public class TrustedServiceInterceptor {

    private static final Logger log = LoggerFactory.getLogger(TrustedServiceInterceptor.class);

    protected TrustedLoginHandler trustedLoginHandler;

    public void setTrustedLoginHandler(TrustedLoginHandler trustedLoginHandler) {
        this.trustedLoginHandler = trustedLoginHandler;
    }

    protected Object aroundInvoke(ProceedingJoinPoint ctx) throws Throwable {
        // check if remote access from trusted environment
        checkTrustedAccess(ctx);

        return ctx.proceed();
    }

    /**
     * Check if the client is permitted to call the service method.
     *
     * @param ctx context
     */
    protected void checkTrustedAccess(ProceedingJoinPoint ctx) {
        RemoteClientInfo remoteClientInfo = RemoteClientInfo.get();

        if (remoteClientInfo != null
                && ctx instanceof MethodInvocationProceedingJoinPoint) {
            if (!trustedLoginHandler.checkAddress(remoteClientInfo.getAddress())) {
                log.warn("Client is not allowed to call '{}' since IP '{}' is not trusted",
                        ctx.getSignature().toShortString(),
                        remoteClientInfo.getAddress());

                throw new TrustedAccessRequiredException();
            }
        }
    }
}