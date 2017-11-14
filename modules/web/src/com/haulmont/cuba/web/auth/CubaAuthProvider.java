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

package com.haulmont.cuba.web.auth;

import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.security.HttpRequestFilter;
import com.haulmont.cuba.web.security.LoginProvider;
import com.haulmont.cuba.web.security.events.AppLoggedOutEvent;
import com.haulmont.cuba.web.security.events.SessionHeartbeatEvent;
import com.haulmont.cuba.web.security.events.UserConnectedEvent;

import javax.annotation.Nullable;
import javax.servlet.Filter;
import java.util.Locale;

/**
 * @deprecated Use {@link LoginProvider} and/or {@link HttpRequestFilter} instead.
 */
@Deprecated
public interface CubaAuthProvider extends Filter {
    String NAME = "cuba_AuthProvider";

    /**
     * Login procedure with user and password.
     *
     * @deprecated Use {@link LoginProvider} instead.
     *
     * @param login          User login
     * @param password       User password
     * @param messagesLocale Locale for error messages
     * @throws LoginException Login exception
     */
    @Deprecated
    void authenticate(String login, String password, Locale messagesLocale) throws LoginException;

    /**
     * Logout from external authentication
     *
     * @deprecated Use application event handler for {@link AppLoggedOutEvent} instead.
     *
     * @return target url of external identity provider or null
     */
    @Nullable
    @Deprecated
    default String logout() {
        return null;
    }

    /**
     * Handler for user session logged in event. Called by application UI tier when CUBA user session is created.
     * Triggered after standard session initialization, right before UI initialization.
     *
     * @deprecated Use application event handler for {@link UserConnectedEvent} instead.
     *
     * @param userSession user session
     */
    default void userSessionLoggedIn(UserSession userSession) {
    }

    /**
     * Send ping to identity provider if supported by auth provider.
     *
     * @deprecated Use application event handler for {@link SessionHeartbeatEvent} instead.
     *
     * @param userSession user session
     */
    default void pingUserSession(UserSession userSession) {
    }
}