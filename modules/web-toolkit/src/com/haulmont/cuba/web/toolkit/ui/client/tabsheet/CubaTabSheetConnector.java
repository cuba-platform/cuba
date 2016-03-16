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

package com.haulmont.cuba.web.toolkit.ui.client.tabsheet;

import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.haulmont.cuba.web.toolkit.ui.CubaTabSheet;
import com.haulmont.cuba.web.toolkit.ui.client.action.RemoteAction;
import com.haulmont.cuba.web.toolkit.ui.client.action.StaticActionOwner;
import com.vaadin.client.WidgetUtil;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.Action;
import com.vaadin.client.ui.tabsheet.TabsheetConnector;
import com.vaadin.shared.ui.Connect;

/**
 */
@Connect(CubaTabSheet.class)
public class CubaTabSheetConnector extends TabsheetConnector {

    protected CubaTabSheetServerRpc rpc = RpcProxy.create(CubaTabSheetServerRpc.class, this);

    protected int lastContextMenuX = -1;
    protected int lastContextMenuY = -1;

    public CubaTabSheetConnector() {
        registerRpc(CubaTabSheetClientRpc.class, new CubaTabSheetClientRpc() {
            @Override
            public void showTabContextMenu(final int tabIndex, ClientAction[] actions) {
                StaticActionOwner actionOwner = new StaticActionOwner(getConnection(), getConnectorId());

                Action[] contextMenuActions = new Action[actions.length];

                for (int i = 0; i < contextMenuActions.length; i++) {
                    contextMenuActions[i] = new RemoteAction(actions[i], actionOwner) {
                        @Override
                        public void execute() {
                            rpc.performAction(tabIndex, actionId);

                            getConnection().getContextMenu().hide();
                        }
                    };
                }

                actionOwner.setActions(contextMenuActions);

                if (lastContextMenuX >= 0 && lastContextMenuY >= 0) {
                    getConnection().getContextMenu().showAt(actionOwner, lastContextMenuX, lastContextMenuY);

                    lastContextMenuX = -1;
                    lastContextMenuY = -1;
                }
            }
        });
    }

    @Override
    public CubaTabSheetWidget getWidget() {
        return (CubaTabSheetWidget) super.getWidget();
    }

    @Override
    protected void init() {
        super.init();

        getWidget().tabContextMenuHandler = new CubaTabSheetWidget.TabContextMenuHandler() {
            @Override
            public void onContextMenu(int tabIndex, ContextMenuEvent event) {
                lastContextMenuX = WidgetUtil.getTouchOrMouseClientX(event.getNativeEvent());
                lastContextMenuY = WidgetUtil.getTouchOrMouseClientY(event.getNativeEvent());

                if (getState().hasActionsHandlers) {
                    rpc.onTabContextMenu(tabIndex);

                    event.stopPropagation();
                    event.preventDefault();
                }
            }
        };
    }

    @Override
    public CubaTabSheetState getState() {
        return (CubaTabSheetState) super.getState();
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        getWidget().assignAdditionalCellStyles();
    }
}