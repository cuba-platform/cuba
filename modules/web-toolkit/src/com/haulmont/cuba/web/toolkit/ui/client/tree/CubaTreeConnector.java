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

package com.haulmont.cuba.web.toolkit.ui.client.tree;

import com.google.gwt.event.shared.HandlerRegistration;
import com.haulmont.cuba.web.toolkit.ui.CubaTree;
import com.haulmont.cuba.web.toolkit.ui.client.Tools;
import com.vaadin.client.*;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.ShortcutActionHandler;
import com.vaadin.client.ui.VTree;
import com.vaadin.client.ui.tree.TreeConnector;
import com.vaadin.shared.ui.Connect;

import java.util.Collections;
import java.util.List;

@Connect(CubaTree.class)
public class CubaTreeConnector extends TreeConnector implements HasComponentsConnector {

    protected boolean contextMenuSelection = false;

    public CubaTreeConnector() {
        registerRpc(CubaTreeClientRpc.class, new CubaTreeClientRpc() {
            @Override
            public void hideContextMenuPopup() {
                if (getWidget().customContextMenuPopup != null) {
                    getWidget().customContextMenuPopup.hide();
                }
            }
        });
    }

    @Override
    public CubaTreeWidget getWidget() {
        return (CubaTreeWidget) super.getWidget();
    }

    @Override
    public CubaTreeState getState() {
        return (CubaTreeState) super.getState();
    }

    public void setContextMenuSelection(boolean contextMenuSelection) {
        this.contextMenuSelection = contextMenuSelection;
    }

    @Override
    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        super.updateFromUIDL(uidl, client);

        getWidget().setDoubleClickHandling(false);

        getWidget().nodeCaptionsAsHtml = uidl.getBooleanAttribute("nodeCaptionsAsHtml");

        // We may have actions attached to this tree
        if (uidl.getChildCount() > 1) {
            final int cnt = uidl.getChildCount();
            for (int i = 1; i < cnt; i++) {
                UIDL childUidl = uidl.getChildUIDL(i);
                if (childUidl.getTag().equals("shortcuts")) {
                    if (getWidget().getShortcutActionHandler() == null) {
                        getWidget().setShortcutActionHandler(new ShortcutActionHandler(uidl.getId(), client));
                    }
                    getWidget().getShortcutActionHandler().updateActionMap(childUidl);
                }
            }
        }
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        if (stateChangeEvent.hasPropertyChanged("doubleClickMode")) {
            getWidget().doubleClickMode = getState().doubleClickMode;
        }
        if (stateChangeEvent.hasPropertyChanged("nodeCaptionsAsHtml")) {
            getWidget().nodeCaptionsAsHtml = getState().nodeCaptionsAsHtml;
        }
        if (stateChangeEvent.hasPropertyChanged("contextMenu")) {
            if (getState().contextMenu != null) {
                ComponentConnector contextMenu = (ComponentConnector) getState().contextMenu;
                getWidget().customContextMenu = contextMenu.getWidget();
            } else {
                getWidget().customContextMenu = null;
            }
        }
    }

    @Override
    protected boolean isNodeUidl(UIDL childUidl) {
        return !"shortcuts".equals(childUidl.getTag());
    }

    @Override
    protected boolean isPopupSelection(UIDL uidl) {
        boolean selection = contextMenuSelection;
        contextMenuSelection = false;
        return selection;
    }

    @Override
    protected VTree.TreeNode createNode(UIDL childUidl) {
        return getWidget().new CubaTreeNode();
    }

    @Override
    public void updateCaption(ComponentConnector connector) {
        // NOP, not rendered
    }

    @Override
    public List<ComponentConnector> getChildComponents() {
        return Collections.emptyList();
    }

    @Override
    public void setChildComponents(List<ComponentConnector> children) {
    }

    @Override
    public HandlerRegistration addConnectorHierarchyChangeHandler(ConnectorHierarchyChangeEvent.ConnectorHierarchyChangeHandler handler) {
        return ensureHandlerManager().addHandler(
                ConnectorHierarchyChangeEvent.TYPE, handler);
    }

    @Override
    public boolean isUseSimpleModeForTouchDevice() {
        return Tools.isUseSimpleMultiselectForTouchDevice();
    }
}