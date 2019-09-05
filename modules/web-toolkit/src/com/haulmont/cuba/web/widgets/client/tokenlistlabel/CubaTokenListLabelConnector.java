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

package com.haulmont.cuba.web.widgets.client.tokenlistlabel;

import com.haulmont.cuba.web.widgets.CubaTokenListLabel;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.csslayout.CssLayoutConnector;
import com.vaadin.shared.ui.Connect;

@Connect(CubaTokenListLabel.class)
public class CubaTokenListLabelConnector extends CssLayoutConnector {

    @Override
    public CubaTokenListLabelWidget getWidget() {
        return (CubaTokenListLabelWidget) super.getWidget();
    }

    @Override
    public void init() {
        super.init();

        getWidget().handler = new CubaTokenListLabelWidget.TokenListLabelHandler() {
            @Override
            public void remove() {
                getRpcProxy(CubaTokenListLabelServerRpc.class).removeToken();
            }

            @Override
            public void click() {
                getRpcProxy(CubaTokenListLabelServerRpc.class).itemClick();
            }
        };
    }

    @Override
    public CubaTokenListLabelState getState() {
        return (CubaTokenListLabelState) super.getState();
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        if (stateChangeEvent.hasPropertyChanged("editable")) {
            getWidget().setEditable(getState().editable);
        }

        if (stateChangeEvent.hasPropertyChanged("canOpen")) {
            getWidget().setCanOpen(getState().canOpen);
        }

        if (stateChangeEvent.hasPropertyChanged("text")) {
            getWidget().setText(getState().text);
        }
    }
}