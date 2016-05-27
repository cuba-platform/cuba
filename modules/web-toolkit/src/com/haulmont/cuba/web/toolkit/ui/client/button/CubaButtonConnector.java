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
 *
 */

package com.haulmont.cuba.web.toolkit.ui.client.button;

import com.google.gwt.event.dom.client.ClickEvent;
import com.haulmont.cuba.web.toolkit.ui.CubaButton;
import com.haulmont.cuba.web.toolkit.ui.client.appui.ValidationErrorHolder;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.button.ButtonConnector;
import com.vaadin.shared.ui.Connect;

@Connect(value = CubaButton.class, loadStyle = Connect.LoadStyle.EAGER)
public class CubaButtonConnector extends ButtonConnector {

    protected boolean pendingResponse = false;

    public CubaButtonConnector() {
        registerRpc(CubaButtonClientRpc.class, new CubaButtonClientRpc() {
            @Override
            public void onClickHandled() {
                stopResponsePending();
            }
        });
    }

    @Override
    public CubaButtonState getState() {
        return (CubaButtonState) super.getState();
    }

    @Override
    public CubaButtonWidget getWidget() {
        return (CubaButtonWidget) super.getWidget();
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        stopResponsePending();

        super.onStateChanged(stateChangeEvent);

        if (stateChangeEvent.hasPropertyChanged("caption")) {
            String text = getState().caption;
            if (text == null || "".equals(text)) {
                getWidget().addStyleDependentName("empty-caption");
            } else {
                getWidget().removeStyleDependentName("empty-caption");
            }
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        if (ValidationErrorHolder.hasValidationErrors()) {
            return;
        }

        if (pendingResponse) {
            return;
        }

        if (getState().useResponsePending) {
            startResponsePending();
        }

        super.onClick(event);
    }

    public void stopResponsePending() {
        pendingResponse = false;
        getWidget().removeStyleDependentName("wait");
    }

    protected void startResponsePending() {
        pendingResponse = true;
        getWidget().addStyleDependentName("wait");
    }
}