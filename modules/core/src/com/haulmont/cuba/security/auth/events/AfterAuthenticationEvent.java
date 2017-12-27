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

package com.haulmont.cuba.security.auth.events;

import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.security.auth.AuthenticationDetails;
import com.haulmont.cuba.security.auth.AuthenticationManager;
import com.haulmont.cuba.security.auth.Credentials;
import com.haulmont.cuba.security.global.UserSession;
import org.springframework.context.ApplicationEvent;

import javax.annotation.Nullable;

/**
 * Event that is fired by {@link AuthenticationManager} after user is authenticated or authentication failed. There is
 * an active database transaction when the event is published. Note that: in case of exception from the database this
 * transaction could be marked as rollback-only.
 * <br>
 * Event is fired when special instance of {@link SecurityContext} is set to {@link AppContext}. It has system user
 * session that has full permissions in the system.
 * <br>
 * Event handlers may throw LoginException to abort authentication process.
 *
 * @see UserSession#isSystem()
 * @see BeforeAuthenticationEvent
 */
public class AfterAuthenticationEvent extends ApplicationEvent {

    protected final AuthenticationDetails authenticationDetails;

    public AfterAuthenticationEvent(Credentials source, @Nullable AuthenticationDetails authenticationDetails) {
        super(source);
        this.authenticationDetails = authenticationDetails;
    }

    @Override
    public Credentials getSource() {
        return (Credentials) super.getSource();
    }

    public Credentials getCredentials() {
        return (Credentials) super.getSource();
    }

    @Nullable
    public AuthenticationDetails getAuthenticationDetails() {
        return authenticationDetails;
    }
}