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

package com.haulmont.cuba.web.toolkit.ui.client.window;

import com.google.gwt.user.client.Event;
import com.haulmont.cuba.web.toolkit.ui.CubaWindow;
import com.haulmont.cuba.web.toolkit.ui.client.action.RemoteAction;
import com.haulmont.cuba.web.toolkit.ui.client.action.StaticActionOwner;
import com.haulmont.cuba.web.toolkit.ui.client.tabsheet.ClientAction;
import com.vaadin.client.WidgetUtil;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.ui.Action;
import com.vaadin.client.ui.window.WindowConnector;
import com.vaadin.shared.ui.Connect;

/**
 */
@Connect(CubaWindow.class)
public class CubaWindowConnector extends WindowConnector {

    protected CubaWindowServerRpc rpc = RpcProxy.create(CubaWindowServerRpc.class, this);

    protected int lastContextMenuX = -1;
    protected int lastContextMenuY = -1;

    public CubaWindowConnector() {
        registerRpc(CubaWindowClientRpc.class, new CubaWindowClientRpc() {
            @Override
            public void showTabContextMenu(ClientAction[] actions) {
                StaticActionOwner actionOwner = new StaticActionOwner(getConnection(), getConnectorId());

                Action[] contextMenuActions = new Action[actions.length];

                for (int i = 0; i < contextMenuActions.length; i++) {
                    contextMenuActions[i] = new RemoteAction(actions[i], actionOwner) {
                        @Override
                        public void execute() {
                            rpc.performContextMenuAction(actionId);

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
    public CubaWindowState getState() {
        return (CubaWindowState) super.getState();
    }

    @Override
    public CubaWindowWidget getWidget() {
        return (CubaWindowWidget) super.getWidget();
    }

    @Override
    protected void init() {
        super.init();

        getWidget().contextMenuHandler = new CubaWindowWidget.ContextMenuHandler() {
            @Override
            public void onContextMenu(Event event) {
                lastContextMenuX = WidgetUtil.getTouchOrMouseClientX(event);
                lastContextMenuY = WidgetUtil.getTouchOrMouseClientY(event);

                if (getState().hasContextActionHandlers) {
                    rpc.onWindowContextMenu();

                    event.stopPropagation();
                    event.preventDefault();
                }
            }
        };
    }
}