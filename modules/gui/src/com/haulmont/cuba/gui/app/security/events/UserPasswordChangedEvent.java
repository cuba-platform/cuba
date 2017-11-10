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

package com.haulmont.cuba.gui.app.security.events;

import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.security.entity.User;
import org.springframework.context.ApplicationEvent;

/**
 * An event that is fired when the password of a user has been changed.
 * The event is fired synchronously from UI thread.
 */
public class UserPasswordChangedEvent extends ApplicationEvent {

    private final User user;
    private final String newPassword;

    public UserPasswordChangedEvent(Window window, User user, String newPassword) {
        super(window);
        this.user = user;
        this.newPassword = newPassword;
    }

    @Override
    public Window getSource() {
        return (Window) super.getSource();
    }

    public User getUser() {
        return user;
    }

    public String getNewPassword() {
        return newPassword;
    }
}