/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.tabsheet;

import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.vaadin.client.ui.VTabsheet;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaTabSheetWidget extends VTabsheet {

    protected TabContextMenuHandler tabContextMenuHandler;

    @Override
    protected void onTabContextMenu(final int tabIndex, ContextMenuEvent event) {
        if (tabContextMenuHandler != null)
            tabContextMenuHandler.onContextMenu(tabIndex, event);
    }

    public interface TabContextMenuHandler {
        void onContextMenu(final int tabIndex, ContextMenuEvent event);
    }
}