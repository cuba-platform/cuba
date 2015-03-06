/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.window;

import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.haulmont.cuba.web.toolkit.ui.client.appui.ValidationErrorHolder;
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

    @Override
    public void onKeyUp(KeyUpEvent event) {
        // disabled Vaadin close by ESCAPE #PL-4355
    }

    @Override
    protected void constructDOM() {
        super.constructDOM();

        DOM.sinkEvents(closeBox, Event.FOCUSEVENTS);
    }

    @Override
    protected void onCloseClick() {
        if (ValidationErrorHolder.hasValidationErrors()) {
            return;
        }

        super.onCloseClick();
    }
}