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

package com.haulmont.cuba.web.widgets;

import com.haulmont.cuba.web.widgets.client.capslockindicator.CubaCapsLockIndicatorState;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Label;

public class CubaCapsLockIndicator extends Label {

    public CubaCapsLockIndicator() {
        initCapsLockIndicatorContent();
    }

    @Override
    protected CubaCapsLockIndicatorState getState() {
        return (CubaCapsLockIndicatorState) super.getState();
    }

    @Override
    protected CubaCapsLockIndicatorState getState(boolean markAsDirty) {
        return (CubaCapsLockIndicatorState) super.getState(markAsDirty);
    }

    protected void initCapsLockIndicatorContent() {
        getState().contentMode = ContentMode.HTML;
        getState().text = "<span></span>";
    }

    public void setCapsLockOnMessage(String capsLockOnMessage) {
        getState().capsLockOnMessage = capsLockOnMessage;
    }

    public String getCapsLockOnMessage() {
        return getState(false).capsLockOnMessage;
    }

    public void setCapsLockOffMessage(String capsLockOffMessage) {
        getState().capsLockOffMessage = capsLockOffMessage;
    }

    public String getCapsLockOffMessage() {
        return getState(false).capsLockOffMessage;
    }
}