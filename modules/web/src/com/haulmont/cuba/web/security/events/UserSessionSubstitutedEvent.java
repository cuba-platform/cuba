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

import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.Connection;
import org.springframework.context.ApplicationEvent;

/**
 * Event that is fired by {@link Connection} when session user has been substituted on middleware right before firing
 * {@link Connection.StateChangeListener} listeners.
 */
public class UserSessionSubstitutedEvent extends ApplicationEvent {
    protected final UserSession sourceSession;
    protected final UserSession substitutedSession;

    public UserSessionSubstitutedEvent(Connection source, UserSession sourceSession, UserSession substitutedSession) {
        super(source);
        this.substitutedSession = substitutedSession;
        this.sourceSession = sourceSession;
    }

    @Override
    public Connection getSource() {
        return (Connection) super.getSource();
    }

    public Connection getConnection() {
        return (Connection) super.getSource();
    }

    public UserSession getSourceSession() {
        return sourceSession;
    }

    public UserSession getSubstitutedSession() {
        return substitutedSession;
    }
}