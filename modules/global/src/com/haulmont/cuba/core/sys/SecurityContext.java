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

import com.haulmont.bali.util.Preconditions;
import com.haulmont.cuba.security.global.UserSession;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * Holds information about the current user session.
 * <br>
 * Instances of this class are normally set in {@link AppContext} by the framework, but also have to be
 * passed to it in case of manually running new threads. Here is the sample code for an asynchronous execution:
 * <pre>{@code
 *     final SecurityContext securityContext = AppContext.getSecurityContext();
 *     executor.submit(new Runnable() {
 *         public void run() {
 *             AppContext.setSecurityContext(securityContext);
 *             // business logic here
 *         }
 *     });
 * }</pre>
 * <br>
 * The same can be done using {@link SecurityContextAwareRunnable} or {@link SecurityContextAwareCallable}
 * wrappers, for example:
 * <pre>{@code
 *     executor.submit(new SecurityContextAwareRunnable<>(() -> {
 *         // business logic here
 *     }));
 * }</pre>
 * or
 * <pre>{@code
 *     Future<String> future = executor.submit(new SecurityContextAwareCallable<>(() -> {
 *         // business logic here
 *         return some_string;
 *     }));
 * }</pre>
 */
public class SecurityContext {

    private final UUID sessionId;
    private UserSession session;
    private String user;
    private int serviceInvocationCount;

    private boolean authorizationRequired;

    public SecurityContext(UUID sessionId) {
        Preconditions.checkNotNullArgument(sessionId, "sessionId is null");
        this.sessionId = sessionId;
    }

    public SecurityContext(UUID sessionId, String user) {
        this(sessionId);
        this.user = user;
    }

    public SecurityContext(UserSession session) {
        Preconditions.checkNotNullArgument(session, "session is null");
        this.session = session;
        this.sessionId = session.getId();
        this.user = session.getUser().getLogin();
    }

    /**
     * @return Current {@link UserSession} ID. This is the only required value for the {@link SecurityContext}.
     */
    public UUID getSessionId() {
        return sessionId;
    }

    /**
     * @return current user session. Can be null, so don't rely on this method in application code - use
     * {@link com.haulmont.cuba.core.global.UserSessionSource}
     */
    @Nullable
    public UserSession getSession() {
        return session;
    }

    /**
     * @return current user login. Can be null, so don't rely on this method in application code - use
     * {@link com.haulmont.cuba.core.global.UserSessionSource}
     */
    @Nullable
    public String getUser() {
        return user;
    }

    /**
     * @deprecated Use isAuthorizationRequired() method of Load/CommitContext
     */
    @Deprecated
    public boolean isAuthorizationRequired() {
        return authorizationRequired;
    }

    /**
     * @deprecated Use setAuthorizationRequired() method of Load/CommitContext
     */
    @Deprecated
    public void setAuthorizationRequired(boolean authorizationRequired) {
        this.authorizationRequired = authorizationRequired;
    }

    /**
     * INTERNAL. Increment service invocation counter.
     */
    int incServiceInvocation() {
        return serviceInvocationCount++;
    }

    /**
     * INTERNAL. Decrement service invocation counter.
     */
    int decServiceInvocation() {
        return serviceInvocationCount--;
    }

    @Override
    public String toString() {
        return "SecurityContext{" +
                "sessionId=" + sessionId +
                '}';
    }
}