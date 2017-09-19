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

import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;

/**
 * Authentication layer for client applications.
 */
public interface AuthenticationService {
    String NAME = "cuba_AuthenticationService";

    /**
     * Authenticates a user, starts session and provides authentication details.
     * Obtained session can be used for service requests.
     *
     * @param credentials credentials
     * @return authentication details
     * @throws LoginException if authentication fails
     */
    AuthenticationDetails login(Credentials credentials) throws LoginException;

    /**
     * Authenticates a user and provides authentication details. Does not start session.
     * Obtained session cannot be used for service requests.
     *
     * @param credentials credentials
     * @return authentication details
     * @throws LoginException if authentication fails
     */
    AuthenticationDetails authenticate(Credentials credentials) throws LoginException;

    /**
     * Log out and destroy an active user session.
     */
    void logout();

    /**
     * Substitute a user, obtaining all its security related environment.
     * <br>
     * This method replaces an active UserSession with the new one, which is returned.
     *
     * @param substitutedUser a user to substitute. Must be in the current users' {@link User#substitutions} list.
     * @return new UserSession instance that contains: <ul>
     * <li> id - the previously active user session id </li>
     * <li> user - the logged in user </li>
     * <li> substitutedUser - the user passed to this method  </li>
     * <li> all security data - loaded for the substitutedUser </li>
     * </ul>
     */
    UserSession substituteUser(User substitutedUser);
}