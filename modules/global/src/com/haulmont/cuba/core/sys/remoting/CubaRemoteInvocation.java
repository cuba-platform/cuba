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
package com.haulmont.cuba.core.sys.remoting;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.remoting.support.RemoteInvocation;

import java.util.UUID;

/**
 * Encapsulates a remote invocation of a middleware service.
 * Additionally transfers the current user session identifier and request scope locale (for anonymous sessions).
 */
public class CubaRemoteInvocation extends RemoteInvocation {

    private static final long serialVersionUID = 5460262566597755733L;

    private UUID sessionId;
    private String locale;

    public CubaRemoteInvocation(MethodInvocation methodInvocation, UUID sessionId) {
        super(methodInvocation);
        this.sessionId = sessionId;
    }

    public CubaRemoteInvocation(MethodInvocation methodInvocation, UUID sessionId, String locale) {
        super(methodInvocation);
        this.sessionId = sessionId;
        this.locale = locale;
    }

    public UUID getSessionId() {
        return sessionId;
    }

    public String getLocale() {
        return locale;
    }
}