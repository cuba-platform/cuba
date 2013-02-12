/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui.client.tabsheet;

import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.vaadin.client.ui.VTabsheet;

/**
 * @author artamonov
 * @version $Id$
 */
public class ActionsTabSheetWidget extends VTabsheet {

    public static final String CLASSNAME = "v-actions-tabsheet";

    protected TabContextMenuHandler tabContextMenuHandler;

    public ActionsTabSheetWidget() {
        setStyleName(CLASSNAME);
    }

    @Override
    protected void onTabContextMenu(final int tabIndex, ContextMenuEvent event) {
        if (tabContextMenuHandler != null)
            tabContextMenuHandler.onContextMenu(tabIndex, event);
    }

    public interface TabContextMenuHandler {
        void onContextMenu(final int tabIndex, ContextMenuEvent event);
    }
}