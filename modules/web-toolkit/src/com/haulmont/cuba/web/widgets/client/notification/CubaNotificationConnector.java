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

package com.haulmont.cuba.web.widgets.client.notification;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.vaadin.client.ui.NotificationDelegate;
import com.vaadin.client.ui.VNotification;
import com.vaadin.client.ui.notification.NotificationConnector;
import com.vaadin.shared.ui.Connect;
import com.vaadin.ui.Notification;

@Connect(value = Notification.class)
public class CubaNotificationConnector extends NotificationConnector {

    public static final String CUBA_NOTIFICATION_MODALITY_CURTAIN = "c-notification-modalitycurtain";

    @Override
    protected NotificationDelegate getDelegate() {
        return new CubaNotificationDelegate();
    }

    public class CubaNotificationDelegate implements NotificationDelegate {

        private Element modalityCurtain;

        @Override
        public void show(Element overlayContainer, Element element, boolean isShowing, String style, int index) {
            if (style != null && (style.contains("error") || style.contains("warning"))) {
                showModalityCurtain(overlayContainer, element, isShowing, index);
            }
        }

        @Override
        public void hide() {
            hideModalityCurtain();
        }

        protected com.google.gwt.user.client.Element getModalityCurtain() {
            if (modalityCurtain == null) {
                modalityCurtain = DOM.createDiv();
                modalityCurtain.setClassName(CUBA_NOTIFICATION_MODALITY_CURTAIN);
            }
            return DOM.asOld(modalityCurtain);
        }

        protected void showModalityCurtain(Element overlayContainer, Element element, boolean isShowing, int index) {
            getModalityCurtain().getStyle().setZIndex(index + VNotification.Z_INDEX_BASE);

            if (isShowing) {
                overlayContainer.insertBefore(getModalityCurtain(), element);
            } else {
                overlayContainer.appendChild(getModalityCurtain());
            }
        }

        protected void hideModalityCurtain() {
            if (modalityCurtain != null) {
                modalityCurtain.removeFromParent();
                modalityCurtain = null;
            }
        }
    }
}
