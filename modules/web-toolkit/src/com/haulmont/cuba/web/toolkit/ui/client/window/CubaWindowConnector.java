/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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
 * @author artamonov
 * @version $Id$
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