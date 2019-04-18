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

        if (stateChangeEvent.hasPropertyChanged("sandbox")) {
            getWidget().setAttribute("sandbox", getState().sandbox);
        }

        if (stateChangeEvent.hasPropertyChanged("srcdoc")) {
            getWidget().setAttribute("srcdoc", getState().srcdoc);
        }

        if (stateChangeEvent.hasPropertyChanged("allow")) {
            getWidget().setAttribute("allow", getState().allow);
        }

        if (stateChangeEvent.hasPropertyChanged("referrerpolicy")) {
            getWidget().setAttribute("referrerpolicy", getState().referrerpolicy);
        }
    }
}
