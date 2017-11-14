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

import com.haulmont.cuba.security.auth.AuthenticationProvider;
import com.haulmont.cuba.security.auth.Credentials;
import com.haulmont.cuba.security.global.LoginException;
import org.springframework.context.ApplicationEvent;

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