/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.toolkit.gwt.client.ui;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.impl.FocusImpl;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.UIDL;
import com.vaadin.terminal.gwt.client.ui.ShortcutActionHandler;
import com.vaadin.terminal.gwt.client.ui.VOrderedLayout;
import com.vaadin.terminal.gwt.client.ui.VVerticalLayout;

/**
 * @version $Id$
 *
 * @author Nikolay Gorodnov
 */
public class VOrderedActionsLayout extends VOrderedLayout {

    protected ShortcutActionHandler shortcutHandler;

    static final FocusImpl impl = FocusImpl.getFocusImplForPanel();

    protected VOrderedActionsLayout(String className, int orientation) {
        super(className, orientation);
        DOM.sinkEvents(getElement(), Event.ONKEYDOWN);
    }

    protected Element createElement() {
        return impl.createFocusable();
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
                    if (shortcutHandler == null) {
                        shortcutHandler = new ShortcutActionHandler(uidl.getId(), client);
                    }
                    shortcutHandler.updateActionMap(childUidl);
                }
            }
        }
    }

    @Override
    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);

        final int type = DOM.eventGetType(event);
        if (type == Event.ONKEYDOWN && shortcutHandler != null) {
            shortcutHandler.handleKeyboardEvent(event);
        }
    }
}
