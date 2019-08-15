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

package com.haulmont.cuba.web.widgets.client.popupview;

import com.haulmont.cuba.web.widgets.CubaPopupView;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.popupview.PopupViewConnector;
import com.vaadin.shared.ui.Connect;

@Connect(CubaPopupView.class)
public class CubaPopupViewConnector extends PopupViewConnector {

    @Override
    public CubaPopupViewWidget getWidget() {
        return (CubaPopupViewWidget) super.getWidget();
    }

    @Override
    public CubaPopupViewState getState() {
        return (CubaPopupViewState) super.getState();
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        CubaPopupViewState state = getState();

        if (stateChangeEvent.hasPropertyChanged("popupPosition")) {
            getWidget().setupPopupPosition(state.popupPosition);
        }

        if (stateChangeEvent.hasPropertyChanged("popupPositionTop")) {
            getWidget().setupPopupPositionTop(state.popupPositionTop);
        }

        if (stateChangeEvent.hasPropertyChanged("popupPositionLeft")) {
            getWidget().setupPopupPositionLeft(state.popupPositionLeft);
        }
    }
}
