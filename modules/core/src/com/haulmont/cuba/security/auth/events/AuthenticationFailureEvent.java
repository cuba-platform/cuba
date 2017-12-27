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
import com.haulmont.cuba.security.auth.AuthenticationManager;
import com.haulmont.cuba.security.auth.AuthenticationProvider;
import com.haulmont.cuba.security.auth.Credentials;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import org.springframework.context.ApplicationEvent;

/**
 * Event that is fired by {@link AuthenticationManager} if user authentication failed against
 * one of {@link AuthenticationProvider}s, i.e. {@link AuthenticationProvider} has thrown exception. There is an active
 * database transaction when the event is published. Note that: in case of exception from the database this transaction
 * could be marked as rollback-only.
 * <br>
 * Event is fired when special instance of {@link SecurityContext} is set to {@link AppContext}. It has system user
 * session that has full permissions in the system.
 * <br>
 * Event handlers may throw custom LoginException instance before caught instance will be rethrown.
 *
 * @see UserSession#isSystem()
 * @see AfterAuthenticationEvent
 */
public class AuthenticationFailureEvent extends ApplicationEvent {

    protected final AuthenticationProvider provider;
    protected final LoginException exception;

    public AuthenticationFailureEvent(Credentials credentials, AuthenticationProvider provider,
                                      LoginException exception) {
        super(credentials);
        this.provider = provider;
        this.exception = exception;
    }

    @Override
    public Credentials getSource() {
        return (Credentials) super.getSource();
    }

    public Credentials getCredentials() {
        return (Credentials) super.getSource();
    }

    public AuthenticationProvider getProvider() {
        return provider;
    }

    public LoginException getException() {
        return exception;
    }
}