/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui.client.orderedactionslayout;

import com.haulmont.cuba.web.toolkit.ui.CubaOrderedActionsLayout;
import com.haulmont.cuba.web.toolkit.ui.client.caption.CubaCaptionWidget;
import com.vaadin.client.*;
import com.vaadin.client.ui.ShortcutActionHandler;
import com.vaadin.client.ui.orderedlayout.AbstractOrderedLayoutConnector;
import com.vaadin.shared.ui.Connect;

/**
 * @author devyatkin
 * @version $Id$
 */
@Connect(value = CubaOrderedActionsLayout.class, loadStyle = Connect.LoadStyle.EAGER)
public class CubaOrderedActionsLayoutConnector extends AbstractOrderedLayoutConnector implements Paintable {

    @Override
    public CubaOrderedActionsLayoutWidget getWidget() {
        return (CubaOrderedActionsLayoutWidget) super.getWidget();
    }

    @Override
    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        final int cnt = uidl.getChildCount();
        for (int i = 0; i < cnt; i++) {
            UIDL childUidl = uidl.getChildUIDL(i);
            if (childUidl.getTag().equals("actions")) {
                if (getWidget().getShortcutHandler() == null) {
                    getWidget().setShortcutHandler(new ShortcutActionHandler(uidl.getId(), client));
                }
                getWidget().getShortcutHandler().updateActionMap(childUidl);
            }
        }
    }

    @Override
    protected void updateCaptionInternal(ComponentConnector child) {
        CubaOrderedLayoutSlot slot = (CubaOrderedLayoutSlot) getWidget().getSlot(child.getWidget());
        if (VCaption.isNeeded(child.getState())) {
            VCaption caption = slot.getCaption();
            if (caption == null) {
                // use our own caption widget
                caption = new CubaCaptionWidget(child, getConnection());
                slot.setCaption(caption);
            }
            caption.updateCaption();
        } else {
            slot.setCaption(null);
        }
    }
}