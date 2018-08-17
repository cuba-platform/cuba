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

package com.haulmont.cuba.web.sys;

import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.ContentMode;
import com.haulmont.cuba.gui.executors.BackgroundWorker;
import com.haulmont.cuba.web.AppUI;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component(Notifications.NAME)
@Scope(UIScope.NAME)
public class WebNotifications implements Notifications {

    public static final int HUMANIZED_NOTIFICATION_DELAY_MSEC = 3000;
    public static final int WARNING_NOTIFICATION_DELAY_MSEC = -1;

    protected AppUI ui;

    protected BackgroundWorker backgroundWorker;

    public WebNotifications(AppUI ui) {
        this.ui = ui;
    }

    @Inject
    protected void setBackgroundWorker(BackgroundWorker backgroundWorker) {
        this.backgroundWorker = backgroundWorker;
    }

    @Override
    public Notification create() {
        backgroundWorker.checkUIAccess();

        return new NotificationImpl();
    }

    public class NotificationImpl implements Notification {
        protected String caption;
        protected String description;
        protected String styleName;

        protected Position position = Position.MIDDLE_CENTER;

        protected ContentMode contentMode = ContentMode.TEXT;
        protected NotificationType notificationType = NotificationType.HUMANIZED;

        public NotificationImpl() {
        }

        @Override
        public Notification setCaption(String caption) {
            this.caption = caption;
            return this;
        }

        @Override
        public String getCaption() {
            return caption;
        }

        @Override
        public Notification setDescription(String description) {
            this.description = description;
            return this;
        }

        @Override
        public String getDescription() {
            return description;
        }

        @Override
        public Notification setType(NotificationType notificationType) {
            this.notificationType = notificationType;

            // todo set default position here

            // todo set default timeout here

            return this;
        }

        @Override
        public NotificationType getType() {
            return notificationType;
        }

        @Override
        public Notification setContentMode(ContentMode contentMode) {
            this.contentMode = contentMode;
            return this;
        }

        @Override
        public ContentMode getContentMode() {
            return contentMode;
        }

        @Override
        public Notification setStyleName(String styleName) {
            this.styleName = styleName;
            return this;
        }

        @Override
        public String getStyleName() {
            return styleName;
        }

        @Override
        public Notification setPosition(Position position) {
            this.position = position;
            return this;
        }

        @Override
        public Position getPosition() {
            return position;
        }

        protected com.vaadin.ui.Notification.Type convertType(NotificationType notificationType) {
            switch (notificationType) {
                case TRAY:
                    return com.vaadin.ui.Notification.Type.TRAY_NOTIFICATION;

                case HUMANIZED:
                    return com.vaadin.ui.Notification.Type.HUMANIZED_MESSAGE;

                case WARNING:
                    return com.vaadin.ui.Notification.Type.WARNING_MESSAGE;

                case ERROR:
                    return com.vaadin.ui.Notification.Type.ERROR_MESSAGE;

                default:
                    throw new UnsupportedOperationException("Unsupported notification type");
            }
        }

        protected void setNotificationDelayMsec(com.vaadin.ui.Notification notification, NotificationType type) {
            switch (type) {
                case HUMANIZED:
                    notification.setDelayMsec(HUMANIZED_NOTIFICATION_DELAY_MSEC);
                    break;
                case WARNING:
                    notification.setDelayMsec(WARNING_NOTIFICATION_DELAY_MSEC);
                    break;
            }
        }

        @Override
        public void show() {
            com.vaadin.ui.Notification vNotification =
                    new com.vaadin.ui.Notification(caption, description, convertType(notificationType));

            setNotificationDelayMsec(vNotification, notificationType);

            vNotification.setHtmlContentAllowed(contentMode == ContentMode.HTML);
            vNotification.setStyleName(styleName);

            vNotification.setPosition(com.vaadin.shared.Position.valueOf(position.name()));

            vNotification.show(ui.getPage());
        }
    }
}