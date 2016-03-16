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

package com.haulmont.cuba.web.toolkit.ui.client.tokenlistlabel;

import com.haulmont.cuba.web.toolkit.ui.CubaTokenListLabel;
import com.haulmont.cuba.web.toolkit.ui.client.Tools;
import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.UIDL;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.VPanel;
import com.vaadin.client.ui.panel.PanelConnector;
import com.vaadin.shared.ui.Connect;

/**
 */
@Connect(CubaTokenListLabel.class)
public class CubaTokenListLabelConnector extends PanelConnector {

    @Override
    public CubaTokenListLabelWidget getWidget() {
        return (CubaTokenListLabelWidget) super.getWidget();
    }

    @Override
    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        super.updateFromUIDL(uidl, client);

        // replace VPanel class names
        Tools.replaceClassNames(getWidget().captionNode, VPanel.CLASSNAME, CubaTokenListLabelWidget.CLASSNAME);
        Tools.replaceClassNames(getWidget().contentNode, VPanel.CLASSNAME, CubaTokenListLabelWidget.CLASSNAME);
        Tools.replaceClassNames(getWidget().bottomDecoration, VPanel.CLASSNAME, CubaTokenListLabelWidget.CLASSNAME);
        Tools.replaceClassNames(getWidget().getElement(), VPanel.CLASSNAME, CubaTokenListLabelWidget.CLASSNAME);
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
        getWidget().setEditable(getState().editable);
        getWidget().setCanOpen(getState().canOpen);
        getWidget().setText(getState().text);
    }
}