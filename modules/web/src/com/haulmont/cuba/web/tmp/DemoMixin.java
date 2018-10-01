/*
 * Copyright (c) 2008-2018 Haulmont.
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

package com.haulmont.cuba.web.tmp;

import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.screen.Extensions;
import com.haulmont.cuba.gui.screen.FrameOwner;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.Subscribe;

public interface DemoMixin extends FrameOwner {
    @Subscribe
    default void onInit(Screen.InitEvent initEvent) {
        Extensions.register(this, State.class, new State("Bye !"));
    }

    @Subscribe
    default void onClose(Screen.AfterCloseEvent closeEvent) {
        String message = Extensions.get(this, State.class).getMessage();
        Notifications notifications = this.getScreenContext().getNotifications();

        notifications.create()
                .setDescription("Closed: " + message)
                .setType(Notifications.NotificationType.TRAY)
                .show();
    }

    class State {
        private String message;

        public State(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}