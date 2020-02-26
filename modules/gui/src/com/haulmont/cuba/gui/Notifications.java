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
import com.haulmont.cuba.gui.components.HasUserOriginated;

import java.util.function.Consumer;

/**
 * Notifications API.
 */
public interface Notifications {

    int DELAY_FOREVER = -1;
    int DELAY_NONE = 0;
    int DELAY_DEFAULT = Integer.MIN_VALUE;

    /**
     * Creates a notification builder.
     * <br>
     * Example of showing a notification:
     * <pre>{@code
     * notifications.create()
     *         .withType(NotificationType.WARNING)
     *         .withCaption("Alert")
     *         .withPosition(Notifications.Position.BOTTOM_RIGHT)
     *         .show();
     * }</pre>
     *
     * @return notification builder
     */
    NotificationBuilder create();

    /**
     * Creates a notification builder with the passed notification type.
     * <br>
     * Example of showing a notification:
     * <pre>{@code
     * notifications.create(NotificationType.WARNING)
     *         .withCaption("Alert")
     *         .withDescription("Please specify name")
     *         .withPosition(Notifications.Position.BOTTOM_RIGHT)
     *         .show();
     * }</pre>
     *
     * @param type notification type
     * @return notification builder
     */
    NotificationBuilder create(NotificationType type);

    /**
     * Notification builder object.
     */
    interface NotificationBuilder {
        /**
         * Sets notification caption.
         *
         * @param caption caption
         * @return this
         */
        NotificationBuilder withCaption(String caption);
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
        NotificationBuilder withDescription(String description);
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
        NotificationBuilder withType(NotificationType type);
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
        NotificationBuilder withContentMode(ContentMode contentMode);
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
        NotificationBuilder withStyleName(String styleName);
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
        NotificationBuilder withPosition(Position position);
        /**
         * @return position
         */
        Position getPosition();

        /**
         * Sets whether html sanitizer is enabled or not for notification content.
         *
         * @param htmlSanitizerEnabled specifies whether html sanitizer is enabled
         * @return builder
         */
        NotificationBuilder withHtmlSanitizer(boolean htmlSanitizerEnabled);

        /**
         * @return html sanitizer is enabled for notification content
         */
        boolean isHtmlSanitizerEnabled();

        /**
         * Sets the delay before the notification disappears.
         *
         * @param hideDelayMs the desired delay in milliseconds, {@value #DELAY_FOREVER} to
         *                    require the user to click the message
         */
        NotificationBuilder withHideDelayMs(int hideDelayMs);
        /**
         * @return the delay before the notification disappears in milliseconds
         */
        int getHideDelayMs();

        /**
         * Sets the listener that will be fired when notification will be closed.
         *
         * @param closeListener {@link CloseEvent} consumer
         * @return this
         */
        NotificationBuilder withCloseListener(Consumer<CloseEvent> closeListener);

        /**
         * @return close listener
         */
        Consumer<CloseEvent> getCloseListener();

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
        ERROR,
        SYSTEM
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

    /**
     * An event that is fired when notification is closed.
     *
     * @see NotificationBuilder#withCloseListener(Consumer)
     */
    class CloseEvent implements HasUserOriginated {

        protected boolean userOriginated;

        public CloseEvent(boolean userOriginated) {
            this.userOriginated = userOriginated;
        }

        @Override
        public boolean isUserOriginated() {
            return userOriginated;
        }
    }
}