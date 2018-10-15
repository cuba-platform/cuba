/*
 * Copyright (c) 2008-2016 Haulmont.
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

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.vaadin.client.WidgetUtil;
import com.vaadin.client.ui.VNotification;

import java.util.List;

import static com.haulmont.cuba.web.widgets.client.notification.CubaNotificationConnector.CUBA_NOTIFICATION_MODALITY_CURTAIN;

public class CubaNotification extends VNotification {
    public static final String TRAY_STYLE = "tray";

    public static final int MARGIN_SIZE = 10;

    @Override
    public boolean onEventPreview(Event event) {
        int type = DOM.eventGetType(event);

        if ((type == Event.ONCLICK || type == Event.ONTOUCHEND)
                && event.getEventTarget() != null) {
            Element target = Element.as(event.getEventTarget());
            if (target.getClassName() != null && target.getClassName().contains(CUBA_NOTIFICATION_MODALITY_CURTAIN)) {
                hide();
                return false;
            }
        }

        if (type == Event.ONKEYDOWN && event.getKeyCode() == KeyCodes.KEY_ESCAPE) {
            if (!getElement().getClassName().contains(TRAY_STYLE)) {
                hide();
                return false;
            }
        }

        return super.onEventPreview(event);
    }

    @Override
    protected void beforeAddNotificationToCollection() {
        if (!isTrayNotification(this)) {
            return;
        }

        List<VNotification> trayList = getCurrentTrayList();

        for (int i = trayList.size() - 1; i >= 0; i--) {
            VNotification notification = trayList.get(i);
            final Element el = notification.getElement();
            if (isTrayNotification(notification)) {
                int notificationPosition = 0;
                try {
                    notificationPosition = Integer.valueOf(el.getStyle()
                            .getBottom().replace("px", ""))
                            + WidgetUtil.getRequiredHeight(getElement());
                } catch (NumberFormatException nex) {
                    notificationPosition += WidgetUtil
                            .getRequiredHeight(getElement());
                }

                notificationPosition += isLastTrayNotification(i) ? (2 * MARGIN_SIZE) : MARGIN_SIZE;
                el.getStyle().setPropertyPx("bottom", notificationPosition);
            }
        }

        trayList.add(this);
    }

    @Override
    protected void afterRemoveNotificationFromCollection(VNotification removedNotification, int removedIdx) {
        if (removedIdx == 0) {
            return;
        }

        if (!isTrayNotification(removedNotification)) {
            return;
        }

        List<VNotification> trayList = getCurrentTrayList();
        trayList.remove(removedNotification);

        Element removedElement = removedNotification.getElement();
        int removedElementHeight = WidgetUtil.getRequiredHeight(removedElement);

        for (int i = removedIdx - 1; i >= 0; i--) {
            VNotification notification = trayList.get(i);
            Element el = notification.getElement();
            if (isTrayNotification(notification)) {
                int notificationPosition = 0;
                if (i == trayList.size() - 1) {
                    notificationPosition = MARGIN_SIZE;
                } else {
                    try {
                        notificationPosition = Integer.valueOf(
                                el.getStyle().getBottom().replace("px", "")) - removedElementHeight;
                        notificationPosition -= MARGIN_SIZE;
                    } catch (NumberFormatException nex) {
                        //
                    }
                }
                el.getStyle().setPropertyPx("bottom", notificationPosition);
            }
        }
    }

    protected boolean isTrayNotification(VNotification notification) {
        return notification.getElement().hasClassName("v-position-bottom")
                && TRAY_STYLE.equals(notification.getTypeStyle());
    }

    protected boolean isLastTrayNotification(int index) {
        List<VNotification> trayList = getCurrentTrayList();

        int lastTrayIndex = 0;
        for (int i = trayList.size() - 1; i > 0; i--) {
            VNotification notification = trayList.get(i);
            if (isTrayNotification(notification)) {
                lastTrayIndex = i;
                break;
            }
        }

        return lastTrayIndex == index;
    }

    protected List<VNotification> getCurrentTrayList() {
        switch (getPosition()) {
            case BOTTOM_LEFT:
                return TRAY_BOTTOM_LEFT_NOTIFICATIONS;
            case BOTTOM_CENTER:
                return TRAY_BOTTOM_CENTER_NOTIFICATIONS;
            case BOTTOM_RIGHT:
                return TRAY_BOTTOM_RIGHT_NOTIFICATIONS;
        }
        return null;
    }
}