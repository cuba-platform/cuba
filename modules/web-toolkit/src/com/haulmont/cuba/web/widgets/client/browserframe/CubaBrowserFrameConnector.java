/*
 * Copyright (c) 2008-2019 Haulmont.
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

package com.haulmont.cuba.web.widgets.client.browserframe;

import com.haulmont.cuba.web.widgets.CubaBrowserFrame;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.browserframe.BrowserFrameConnector;
import com.vaadin.shared.ui.Connect;

import static com.haulmont.cuba.web.widgets.client.browserframe.CubaBrowserFrameState.ALLOW;
import static com.haulmont.cuba.web.widgets.client.browserframe.CubaBrowserFrameState.REFERRERPOLICY;
import static com.haulmont.cuba.web.widgets.client.browserframe.CubaBrowserFrameState.SANDBOX;
import static com.haulmont.cuba.web.widgets.client.browserframe.CubaBrowserFrameState.SRCDOC;

@Connect(CubaBrowserFrame.class)
public class CubaBrowserFrameConnector extends BrowserFrameConnector {

    @Override
    public CubaBrowserFrameWidget getWidget() {
        return (CubaBrowserFrameWidget) super.getWidget();
    }

    @Override
    public CubaBrowserFrameState getState() {
        return (CubaBrowserFrameState) super.getState();
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        CubaBrowserFrameState state = getState();

        if (stateChangeEvent.hasPropertyChanged(SRCDOC)) {
            getWidget().setSrcdoc(state.srcdoc, getConnectorId());
        }

        if (stateChangeEvent.hasPropertyChanged("resources")
                || stateChangeEvent.hasPropertyChanged(SRCDOC)) {
            getWidget().setAttribute(ALLOW, state.allow);
            getWidget().setAttribute(REFERRERPOLICY, state.referrerpolicy);
            getWidget().setAttribute(SANDBOX, state.sandbox);
        }

        if (stateChangeEvent.hasPropertyChanged(ALLOW)) {
            getWidget().setAttribute(ALLOW, state.allow);
        }

        if (stateChangeEvent.hasPropertyChanged(REFERRERPOLICY)) {
            getWidget().setAttribute(REFERRERPOLICY, state.referrerpolicy);
        }

        if (stateChangeEvent.hasPropertyChanged(SANDBOX)) {
            getWidget().setAttribute(SANDBOX, state.sandbox);
        }
    }
}
