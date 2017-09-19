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

import com.haulmont.cuba.security.auth.Credentials;
import com.haulmont.cuba.security.global.UserSession;
import org.springframework.context.ApplicationEvent;

public class AfterLoginEvent extends ApplicationEvent {

    private final UserSession userSession;

    public AfterLoginEvent(Credentials source, UserSession userSession) {
        super(source);
        this.userSession = userSession;
    }

    @Override
    public Credentials getSource() {
        return (Credentials) super.getSource();
    }

    public UserSession getUserSession() {
        return userSession;
    }
}