/*
 * Copyright (c) 2008-2017 Haulmont.
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

package com.haulmont.cuba.security.app;

import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

/**
 * Service that provides additional API for trusted clients.
 */
public interface TrustedClientService {
    String NAME = "cuba_TrustedClientService";

    /**
     * Get system user session from a trusted client. <br>
     * Do not call {@link com.haulmont.cuba.security.auth.AuthenticationService#logout()} for obtained user session.
     * It is cached on middleware for multiple clients. <br>
     * Do not cache system session on clients since it is not replicated in cluster.
     *
     * @param trustedClientPassword trusted client password
     * @return created user session
     * @throws LoginException if passed invalid trusted client password
     */
    @Nonnull
    UserSession getSystemSession(String trustedClientPassword) throws LoginException;

    /**
     * Get anonymous user session from a trusted client.
     *
     * @param trustedClientPassword trusted client password
     * @return anonymous user session
     * @throws LoginException if passed invalid trusted client password
     */
    @Nonnull
    UserSession getAnonymousSession(String trustedClientPassword) throws LoginException;

    /**
     * Get a UserSession from the cache of currently active sessions.
     *
     * @param trustedClientPassword trusted client password
     * @param sessionId             session id
     * @return a UserSession instance or null, if not found
     * @throws LoginException if passed invalid trusted client password
     */
    @Nullable
    UserSession findSession(String trustedClientPassword, UUID sessionId) throws LoginException;
}