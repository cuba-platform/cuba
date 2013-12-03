/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.tree;

import com.haulmont.cuba.web.toolkit.ui.CubaTree;
import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.UIDL;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.ShortcutActionHandler;
import com.vaadin.client.ui.VTree;
import com.vaadin.client.ui.tree.TreeConnector;
import com.vaadin.shared.ui.Connect;

/**
 * @author devyatkin
 * @version $Id$
 */
@Connect(value = CubaTree.class, loadStyle = Connect.LoadStyle.LAZY)
public class CubaTreeConnector extends TreeConnector {

    @Override
    public CubaTreeWidget getWidget() {
        return (CubaTreeWidget) super.getWidget();
    }

    @Override
    public CubaTreeState getState() {
        return (CubaTreeState) super.getState();
    }

    @Override
    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        super.updateFromUIDL(uidl, client);

        getWidget().setContextMenuHandling(false);
        getWidget().setDoubleClickHandling(false);

        // We may have actions attached to this tree
        if (uidl.getChildCount() > 1) {
            final int cnt = uidl.getChildCount();
            for (int i = 1; i < cnt; i++) {
                UIDL childUidl = uidl.getChildUIDL(i);
                if (childUidl.getTag().equals("actions")) {
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
    }

    @Override
    protected boolean isPopupSelection(UIDL uidl) {
        return Boolean.TRUE.equals(uidl.getBooleanAttribute("popupSelection"));
    }

    @Override
    protected VTree.TreeNode createNode(UIDL childUidl) {
        return getWidget().new CubaTreeNode();
    }
}