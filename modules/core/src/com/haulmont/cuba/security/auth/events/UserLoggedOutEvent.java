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
import com.haulmont.cuba.security.global.UserSession;
import org.springframework.context.ApplicationEvent;

/**
 * Event that is published by {@link AuthenticationManager} after user is logged out and session of the user has been
 * removed from user session storage. There is no active transaction at the moment of event publishing. Event handlers
 * must start a new transaction if they work with database.
 * <br>
 * Event is fired when special instance of {@link SecurityContext} is set to {@link AppContext}. It has system user
 * session that has full permissions in the system.
 *
 * @see UserLoggedInEvent
 * @see UserSession#isSystem()
 */
public class UserLoggedOutEvent extends ApplicationEvent {

    public UserLoggedOutEvent(UserSession source) {
        super(source);
    }

    @Override
    public UserSession getSource() {
        return (UserSession) super.getSource();
    }

    public UserSession getUserSession() {
        return (UserSession) super.getSource();
    }
}