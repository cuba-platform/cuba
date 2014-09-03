/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.tabsheet;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.user.client.ui.Widget;
import com.haulmont.cuba.web.toolkit.ui.CubaTabSheet;
import com.haulmont.cuba.web.toolkit.ui.client.action.RemoteAction;
import com.haulmont.cuba.web.toolkit.ui.client.action.StaticActionOwner;
import com.vaadin.client.Util;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.ui.Action;
import com.vaadin.client.ui.tabsheet.TabsheetConnector;
import com.vaadin.shared.ui.Connect;

/**
 * @author artamonov
 * @version $Id$
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
    protected Widget createWidget() {
        CubaTabSheetWidget widget = GWT.create(CubaTabSheetWidget.class);
        widget.tabContextMenuHandler = new CubaTabSheetWidget.TabContextMenuHandler() {
            @Override
            public void onContextMenu(int tabIndex, ContextMenuEvent event) {
                lastContextMenuX = Util.getTouchOrMouseClientX(event.getNativeEvent());
                lastContextMenuY = Util.getTouchOrMouseClientY(event.getNativeEvent());

                if (getState().hasActionsHandlers) {
                    rpc.onTabContextMenu(tabIndex);

                    event.stopPropagation();
                    event.preventDefault();
                }
            }
        };
        return widget;
    }

    @Override
    public CubaTabSheetState getState() {
        return (CubaTabSheetState) super.getState();
    }
}