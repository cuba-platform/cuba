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
 *
 */

package com.haulmont.cuba.gui;

import com.haulmont.cuba.gui.components.ContentMode;

/**
 * Notifications API.
 */
public interface Notifications {

    String NAME = "cuba_Notifications";

    Notification create();

    /**
     * Notification object.
     */
    interface Notification {
        Notification setCaption(String caption);
        String getCaption();

        Notification setDescription(String description);
        String getDescription();

        Notification setType(NotificationType notificationType);
        NotificationType getType();

        Notification setContentMode(ContentMode contentMode);
        ContentMode getContentMode();

        Notification setStyleName(String styleName);
        String getStyleName();

        Notification setPosition(Position position);
        Position getPosition();

        /**
         * Shows notification.
         */
        void show();
    }

    /**
     * Popup notification type.
     */
    enum NotificationType {
        TRAY,
        HUMANIZED,
        WARNING,
        ERROR
    }

    /**
     * Popup notification position.
     */
    enum Position {
        TOP_LEFT, TOP_CENTER, TOP_RIGHT,
        MIDDLE_LEFT, MIDDLE_CENTER, MIDDLE_RIGHT,
        BOTTOM_LEFT, BOTTOM_CENTER, BOTTOM_RIGHT
    }
}