/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui.client.tree;

import com.haulmont.cuba.web.toolkit.ui.CubaTree;
import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.UIDL;
import com.vaadin.client.ui.ShortcutActionHandler;
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
    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        super.updateFromUIDL(uidl, client);

        // We may have actions attached to this panel
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
}
