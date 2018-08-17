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

    int DELAY_FOREVER = -1;
    int DELAY_NONE = 0;
    int DELAY_DEFAULT = Integer.MIN_VALUE;

    /**
     * Creates empty notification object.
     *
     * @return notification
     */
    Notification create();

    /**
     * Notification object.
     */
    interface Notification {
        /**
         * Sets notification caption.
         *
         * @param caption caption
         * @return this
         */
        Notification setCaption(String caption);
        /**
         * @return caption
         */
        String getCaption();

        /**
         * Sets notification description.
         *
         * @param description description
         * @return this
         */
        Notification setDescription(String description);
        /**
         * @return description
         */
        String getDescription();

        /**
         * Sets type of notification.
         *
         * @param type type
         * @return this
         */
        Notification setType(NotificationType type);
        /**
         * @return type
         */
        NotificationType getType();

        /**
         * Sets content mode for caption and description of notification.
         *
         * @param contentMode content mode
         * @return this
         */
        Notification setContentMode(ContentMode contentMode);
        /**
         * @return content mode
         */
        ContentMode getContentMode();

        /**
         * Sets CSS class name for notification DOM element.
         *
         * @param styleName CSS class name
         * @return this
         */
        Notification setStyleName(String styleName);
        /**
         * @return CSS class name
         */
        String getStyleName();

        /**
         * Sets position of notification.
         *
         * @param position position
         * @return this
         */
        Notification setPosition(Position position);
        /**
         * @return position
         */
        Position getPosition();

        /**
         * Sets the delay before the notification disappears.
         *
         * @param hideDelayMs the desired delay in milliseconds, {@value #DELAY_FOREVER} to
         *                    require the user to click the message
         */
        Notification setHideDelayMs(int hideDelayMs);
        /**
         * @return the delay before the notification disappears in milliseconds
         */
        int getHideDelayMs();

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
        /**
         * Set position by notification type
         */
        DEFAULT,

        TOP_LEFT, TOP_CENTER, TOP_RIGHT,
        MIDDLE_LEFT, MIDDLE_CENTER, MIDDLE_RIGHT,
        BOTTOM_LEFT, BOTTOM_CENTER, BOTTOM_RIGHT
    }
}