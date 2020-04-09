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
import com.haulmont.cuba.web.WebConfig;
import com.haulmont.cuba.web.sys.sanitizer.HtmlSanitizer;
import com.vaadin.ui.Notification;

import javax.inject.Inject;
import java.util.function.Consumer;

public class WebNotifications implements Notifications {

    protected static final int HUMANIZED_NOTIFICATION_DELAY_MSEC = 3000;
    protected static final int WARNING_NOTIFICATION_DELAY_MSEC = -1;

    protected AppUI ui;

    protected BackgroundWorker backgroundWorker;

    @Inject
    protected WebConfig webConfig;
    @Inject
    protected HtmlSanitizer htmlSanitizer;

    public WebNotifications(AppUI ui) {
        this.ui = ui;
    }

    @Inject
    protected void setBackgroundWorker(BackgroundWorker backgroundWorker) {
        this.backgroundWorker = backgroundWorker;
    }

    @Override
    public NotificationBuilder create() {
        backgroundWorker.checkUIAccess();

        return new NotificationBuilderImpl();
    }

    @Override
    public NotificationBuilder create(NotificationType type) {
        return create()
                .withType(type);
    }

    public class NotificationBuilderImpl implements NotificationBuilder {

        public static final String SYSTEM_STYLENAME = "system";

        protected String caption;
        protected String description;
        protected String styleName;

        protected boolean htmlSanitizerEnabled = webConfig.getHtmlSanitizerEnabled();

        protected Position position = Position.DEFAULT;
        protected int hideDelayMs = Integer.MIN_VALUE;

        protected ContentMode contentMode = ContentMode.TEXT;
        protected NotificationType notificationType = NotificationType.HUMANIZED;
        protected Consumer<CloseEvent> closeListener;

        public NotificationBuilderImpl() {
        }

        @Override
        public NotificationBuilder withCaption(String caption) {
            this.caption = caption;
            return this;
        }

        @Override
        public String getCaption() {
            return caption;
        }

        @Override
        public NotificationBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        @Override
        public String getDescription() {
            return description;
        }

        @Override
        public NotificationBuilder withType(NotificationType notificationType) {
            this.notificationType = notificationType;

            return this;
        }

        @Override
        public NotificationType getType() {
            return notificationType;
        }

        @Override
        public NotificationBuilder withContentMode(ContentMode contentMode) {
            if (contentMode == ContentMode.PREFORMATTED) {
                throw new UnsupportedOperationException("ContentMode.PREFORMATTED is not supported for Notification");
            }

            this.contentMode = contentMode;
            return this;
        }

        @Override
        public ContentMode getContentMode() {
            return contentMode;
        }

        @Override
        public NotificationBuilder withStyleName(String styleName) {
            this.styleName = styleName;
            return this;
        }

        @Override
        public String getStyleName() {
            return styleName;
        }

        @Override
        public NotificationBuilder withPosition(Position position) {
            this.position = position;
            return this;
        }

        @Override
        public Position getPosition() {
            return position;
        }

        @Override
        public NotificationBuilder withHtmlSanitizer(boolean htmlSanitizerEnabled) {
            this.htmlSanitizerEnabled = htmlSanitizerEnabled;
            return this;
        }

        @Override
        public boolean isHtmlSanitizerEnabled() {
            return htmlSanitizerEnabled;
        }

        @Override
        public NotificationBuilder withHideDelayMs(int hideDelayMs) {
            this.hideDelayMs = hideDelayMs;
            return this;
        }

        @Override
        public int getHideDelayMs() {
            return hideDelayMs;
        }

        @Override
        public NotificationBuilder withCloseListener(Consumer<CloseEvent> closeListener) {
            this.closeListener = closeListener;
            return this;
        }

        @Override
        public Consumer<CloseEvent> getCloseListener() {
            return closeListener;
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
            if (type == NotificationType.HUMANIZED) {
                notification.setDelayMsec(HUMANIZED_NOTIFICATION_DELAY_MSEC);
            } else if (type == NotificationType.WARNING) {
                notification.setDelayMsec(WARNING_NOTIFICATION_DELAY_MSEC);
            }
        }

        protected com.vaadin.ui.Notification createNotification() {
            com.vaadin.ui.Notification vNotification;

            if (NotificationType.SYSTEM == notificationType) {
                vNotification = new com.vaadin.ui.Notification(caption, description);

                vNotification.setDelayMsec(Notifications.DELAY_FOREVER);
                vNotification.setPosition(com.vaadin.shared.Position.TOP_CENTER);
                vNotification.setStyleName(SYSTEM_STYLENAME);
            } else {
                vNotification = new com.vaadin.ui.Notification(caption, description, convertType(notificationType));

                if (hideDelayMs != DELAY_DEFAULT) {
                    vNotification.setDelayMsec(hideDelayMs);
                } else {
                    setNotificationDelayMsec(vNotification, notificationType);
                }

                if (position != Position.DEFAULT) {
                    vNotification.setPosition(com.vaadin.shared.Position.valueOf(position.name()));
                }

                vNotification.setHtmlContentAllowed(contentMode == ContentMode.HTML);

                if (contentMode == ContentMode.HTML
                        && isHtmlSanitizerEnabled()) {
                    if (caption != null) {
                        vNotification.setCaption(htmlSanitizer.sanitize(getCaption()));
                    }

                    if (description != null) {
                        vNotification.setDescription(htmlSanitizer.sanitize(getDescription()));
                    }
                }

                if (styleName != null) {
                    vNotification.setStyleName(styleName);
                }
            }

            if (closeListener != null) {
                vNotification.addCloseListener(e ->
                        closeListener.accept(new CloseEvent(e.isUserOriginated())));
            }

            return vNotification;
        }

        @Override
        public void show() {
            Notification notification = createNotification();
            notification.show(ui.getPage());
        }
    }
}