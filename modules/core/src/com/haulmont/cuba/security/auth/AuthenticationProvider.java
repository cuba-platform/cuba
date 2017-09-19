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

package com.haulmont.cuba.security.auth;

import com.haulmont.cuba.security.global.LoginException;

import javax.annotation.Nullable;

/**
 * Authentication module that can process a specific {@link Credentials} implementation.
 */
public interface AuthenticationProvider {
    /**
     * Defines the highest precedence for {@link org.springframework.core.Ordered} providers of the platform.
     */
    int HIGHEST_PLATFORM_PRECEDENCE = 100;

    /**
     * Defines the lowest precedence for {@link org.springframework.core.Ordered} providers of the platform.
     */
    int LOWEST_PLATFORM_PRECEDENCE = 1000;

    /**
     * Authenticates a user and provides authentication details. Should not start session.
     * Obtained session cannot be used for service requests.
     *
     * @param credentials credentials
     * @return authentication details
     * @throws LoginException if authentication fails
     */
    @Nullable
    AuthenticationDetails authenticate(Credentials credentials) throws LoginException;

    /**
     * Checks if this provider supports passed credentials class or not.
     *
     * @param credentialsClass credentials class
     * @return true if this provider supports the indicated {@link Credentials} object.
     */
    boolean supports(Class<?> credentialsClass);
}