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

package com.haulmont.cuba.web.auth;

import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;

import javax.annotation.Nullable;
import javax.servlet.Filter;
import java.util.Locale;

public interface CubaAuthProvider extends Filter {
    String NAME = "cuba_AuthProvider";

    /**
     * Login procedure with user and password.
     *
     * @param login          User login
     * @param password       User password
     * @param messagesLocale Locale for error messages
     * @throws LoginException Login exception
     */
    void authenticate(String login, String password, Locale messagesLocale) throws LoginException;

    /**
     * Logout from external authentication
     *
     * @return target url of external identity provider or null
     */
    @Nullable
    default String logout() {
        return null;
    }

    /**
     * Handler for user session logged in event. Called by application UI tier when CUBA user session is created.
     * Triggered after standard session initialization, right before UI initialization.
     *
     * @param userSession user session
     */
    default void userSessionLoggedIn(UserSession userSession) {
    }

    /**
     * Send ping to identity provider if supported by auth provider.
     *
     * @param userSession user session
     */
    default void pingUserSession(UserSession userSession) {
    }
}