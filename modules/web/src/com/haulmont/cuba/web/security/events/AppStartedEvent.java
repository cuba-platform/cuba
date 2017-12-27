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

import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.Connection;
import org.springframework.context.ApplicationEvent;

/**
 * Event that is fired on the first request processing of an {@link App} right before login as anonymous user.
 * <br>
 * Note that: there is no active {@link UserSession} and no {@link SecurityContext} set at the moment of event firing.
 * <br>
 * Event handlers may login user using {@link Connection} object bound to {@link App}.
 */
public class AppStartedEvent extends ApplicationEvent {

    public AppStartedEvent(App source) {
        super(source);
    }

    @Override
    public App getSource() {
        return (App) super.getSource();
    }

    public App getApp() {
        return (App) super.getSource();
    }
}