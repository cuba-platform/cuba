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

import com.haulmont.cuba.security.global.UserSession;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.UUID;

/**
 * Holds information about the current user session.
 *
 * <p/>Instances of this class are normally set in {@link AppContext} by the framework, but also have to be
 * passed to it in case of manually running new threads. Here is the sample code for an asynchronous execution:
 * <pre>
 *     final SecurityContext securityContext = AppContext.getSecurityContext();
 *     executor.submit(new Runnable() {
 *         public void run() {
 *             AppContext.setSecurityContext(securityContext);
 *             // business logic here
 *         }
 *     });
 * </pre>
 *
 */
public class SecurityContext {

    private final UUID sessionId;
    private UserSession session;
    private String user;

    private boolean authorizationRequired;

    public SecurityContext(UUID sessionId) {
        Objects.requireNonNull(sessionId, "sessionId is null");
        this.sessionId = sessionId;
    }

    public SecurityContext(UUID sessionId, String user) {
        this(sessionId);
        this.user = user;
    }

    public SecurityContext(UserSession session) {
        Objects.requireNonNull(session, "session is null");
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
     * @return Whether the security check is required for standard mechanisms ({@code DataManager} in particular) on
     * the middleware
     */
    public boolean isAuthorizationRequired() {
        return authorizationRequired;
    }

    /**
     * Whether the security check is required for standard mechanisms ({@code DataManager} in particular) on
     * the middleware. Example usage:
     * <pre>
     * boolean saved = AppContext.getSecurityContext().isAuthorizationRequired();
     * AppContext.getSecurityContext().setAuthorizationRequired(true);
     * try {
     *     // all calls to DataManager will apply security restrictions
     * } finally {
     *     AppContext.getSecurityContext().setAuthorizationRequired(saved);
     * }
     * </pre>
     */
    public void setAuthorizationRequired(boolean authorizationRequired) {
        this.authorizationRequired = authorizationRequired;
    }

    @Override
    public String toString() {
        return "SecurityContext{" +
                "sessionId=" + sessionId +
                '}';
    }
}
