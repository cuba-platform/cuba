/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.window;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.vaadin.client.ui.VWindow;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaWindowWidget extends VWindow {

    public interface ContextMenuHandler {
        void onContextMenu(Event event);
    }

    protected ContextMenuHandler contextMenuHandler;

    public CubaWindowWidget() {
        DOM.sinkEvents(header, DOM.getEventsSunk(header) | Event.ONCONTEXTMENU);
    }

    @Override
    public void onBrowserEvent(Event event) {
        if (contextMenuHandler != null && event.getTypeInt() == Event.ONCONTEXTMENU) {
            contextMenuHandler.onContextMenu(event);
        }
        super.onBrowserEvent(event);
    }
}