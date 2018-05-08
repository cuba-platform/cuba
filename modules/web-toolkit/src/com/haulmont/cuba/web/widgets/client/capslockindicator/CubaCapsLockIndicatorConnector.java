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

import com.haulmont.cuba.web.widgets.CubaCapsLockIndicator;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.label.LabelConnector;
import com.vaadin.shared.ui.Connect;

@Connect(CubaCapsLockIndicator.class)
public class CubaCapsLockIndicatorConnector extends LabelConnector {

    public CubaCapsLockIndicatorConnector() {
        getWidget().showCapsLockStatus(false); // init caps lock-off status by default
    }

    @Override
    public CubaCapsLockIndicatorWidget getWidget() {
        return (CubaCapsLockIndicatorWidget) super.getWidget();
    }

    @Override
    public CubaCapsLockIndicatorState getState() {
        return (CubaCapsLockIndicatorState) super.getState();
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        if (stateChangeEvent.hasPropertyChanged("capsLockOnMessage")) {
            getWidget().setCapsLockOnMessage(getState().capsLockOnMessage);
        }

        if (stateChangeEvent.hasPropertyChanged("capsLockOffMessage")) {
            getWidget().setCapsLockOffMessage(getState().capsLockOffMessage);
        }
    }
}