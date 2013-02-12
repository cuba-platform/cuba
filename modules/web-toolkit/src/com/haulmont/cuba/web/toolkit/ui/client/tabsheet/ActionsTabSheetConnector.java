/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui.client.tabsheet;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.user.client.ui.Widget;
import com.haulmont.cuba.web.toolkit.ui.ActionsTabSheet;
import com.vaadin.client.Util;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.ui.Action;
import com.vaadin.client.ui.tabsheet.TabsheetConnector;
import com.vaadin.shared.ui.Connect;

/**
 * @author artamonov
 * @version $Id$
 */
@Connect(value = ActionsTabSheet.class)
public class ActionsTabSheetConnector extends TabsheetConnector {

    protected ActionsTabSheetServerRpc rpc = RpcProxy.create(ActionsTabSheetServerRpc.class, this);

    protected int lastContextMenuX = -1;
    protected int lastContextMenuY = -1;

    public ActionsTabSheetConnector() {
        registerRpc(ActionsTabSheetClientRpc.class, new ActionsTabSheetClientRpc() {
            @Override
            public void showTabContextMenu(final int tabIndex, ClientAction[] actions) {
                StaticActionOwner actionOwner = new StaticActionOwner(getConnection(), getWidget().id);

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
    public ActionsTabSheetWidget getWidget() {
        return (ActionsTabSheetWidget) super.getWidget();
    }

    @Override
    protected Widget createWidget() {
        ActionsTabSheetWidget widget = GWT.create(ActionsTabSheetWidget.class);
        widget.tabContextMenuHandler = new ActionsTabSheetWidget.TabContextMenuHandler() {
            @Override
            public void onContextMenu(int tabIndex, ContextMenuEvent event) {
                lastContextMenuX = Util.getTouchOrMouseClientX(event.getNativeEvent());
                lastContextMenuY = Util.getTouchOrMouseClientY(event.getNativeEvent());

                event.stopPropagation();
                event.preventDefault();

                if (getState().hasActionsHanlders)
                    rpc.onTabContextMenu(tabIndex);
            }
        };
        return widget;
    }

    @Override
    public ActionsTabSheetState getState() {
        return (ActionsTabSheetState) super.getState();
    }
}