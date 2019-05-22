/*
 * Copyright (c) 2008-2019 Haulmont.
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

package com.haulmont.cuba.web.testsupport.ui;

import com.haulmont.cuba.security.auth.Credentials;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.Connection;
import com.vaadin.server.VaadinSession;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class TestConnection implements Connection {
    @Override
    public void login(Credentials credentials) throws LoginException {
    }

    @Override
    public void logout() {
    }

    @Nullable
    @Override
    public UserSession getSession() {
        return VaadinSession.getCurrent().getAttribute(UserSession.class);
    }

    @Override
    public void substituteUser(User substitutedUser) {
    }

    @Override
    public boolean isConnected() {
        return true;
    }

    @Override
    public boolean isAuthenticated() {
        return false;
    }

    @Override
    public boolean isAlive() {
        return true;
    }

    @Override
    public void addStateChangeListener(Consumer<StateChangeEvent> listener) {
    }

    @Override
    public void removeStateChangeListener(Consumer<StateChangeEvent> listener) {
    }

    @Override
    public void addUserSubstitutionListener(Consumer<UserSubstitutedEvent> listener) {
    }

    @Override
    public void removeUserSubstitutionListener(Consumer<UserSubstitutedEvent> listener) {
    }
}