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

package com.haulmont.cuba.web.security.events;

import com.haulmont.cuba.security.auth.AuthenticationDetails;
import com.haulmont.cuba.security.auth.Credentials;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.Connection;
import org.springframework.context.ApplicationEvent;

/**
 * Event that is fired after user session is created on middleware.
 */
public class UserSessionStartedEvent extends ApplicationEvent {
    protected final Credentials credentials;
    protected final AuthenticationDetails authenticationDetails;

    public UserSessionStartedEvent(Connection source, Credentials credentials,
                                   AuthenticationDetails authenticationDetails) {
        super(source);
        this.credentials = credentials;
        this.authenticationDetails = authenticationDetails;
    }

    @Override
    public Connection getSource() {
        return (Connection) super.getSource();
    }

    public Connection getConnection() {
        return (Connection) super.getSource();
    }

    public UserSession getUserSession() {
        return authenticationDetails.getSession();
    }

    public AuthenticationDetails getAuthenticationDetails() {
        return authenticationDetails;
    }

    public Credentials getCredentials() {
        return credentials;
    }
}