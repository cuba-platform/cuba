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

package com.haulmont.cuba.web.widgets.client.capslockindicator;

import com.vaadin.client.WidgetUtil;
import com.vaadin.client.ui.VLabel;

public class CubaCapsLockIndicatorWidget extends VLabel implements CapsLockChangeHandler {

    protected String capsLockOnMessage = null;
    protected String capsLockOffMessage = null;

    @Override
    public void showCapsLockStatus(boolean isCapsLock) {
        if (isCapsLock) {
            removeStyleName("capslock-off");
            addStyleName("capslock-on");

            setMessageOn(capsLockOnMessage);
        } else {
            removeStyleName("capslock-on");
            addStyleName("capslock-off");

            setMessageOff(capsLockOffMessage);
        }
    }

    public void setCapsLockOnMessage(String capsLockOnMessage) {
        this.capsLockOnMessage = capsLockOnMessage;

        setMessageOn(capsLockOnMessage);
    }

    public void setCapsLockOffMessage(String capsLockOffMessage) {
        this.capsLockOffMessage = capsLockOffMessage;

        setMessageOff(capsLockOffMessage);
    }

    protected void setMessageOn(String message) {
        if (message == null || message.length() == 0) {
            removeStyleName("message-off");

            setHTML("<span></span>");
        } else {
            removeStyleName("message-off");
            addStyleName("message-on");

            setHTML("<span>" + WidgetUtil.escapeHTML(message) + "</span>");
        }
    }

    protected void setMessageOff(String message) {
        if (message == null || message.length() == 0) {
            removeStyleName("message-on");

            setHTML("<span></span>");
        } else {
            removeStyleName("message-on");
            addStyleName("message-off");

            setHTML("<span>" + WidgetUtil.escapeHTML(message) + "</span>");
        }
    }
}