/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.textfield;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ui.ShortcutActionHandler;
import com.vaadin.client.ui.VTextField;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaTextFieldWidget extends VTextField implements ShortcutActionHandler.ShortcutActionHandlerOwner {

    protected ShortcutActionHandler shortcutHandler;

    protected boolean readOnlyFocusable = false;

    public CubaTextFieldWidget() {
        // handle shortcuts
        DOM.sinkEvents(getElement(), Event.ONKEYDOWN);
    }

    @Override
    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);

        final int type = DOM.eventGetType(event);

        if (type == Event.ONKEYDOWN && shortcutHandler != null) {
            shortcutHandler.handleKeyboardEvent(event);
        }
    }

    public void setShortcutActionHandler(ShortcutActionHandler handler) {
        this.shortcutHandler = handler;
    }

    @Override
    public ShortcutActionHandler getShortcutActionHandler() {
        return shortcutHandler;
    }

    @Override
    public void add(Widget w) {
    }

    @Override
    public void clear() {
    }

    @Override
    public Iterator<Widget> iterator() {
        return new LinkedList<Widget>().iterator();
    }

    @Override
    public boolean remove(Widget w) {
        return false;
    }

    /**
     * If {@code readOnlyFocusable} property is set then component is focusable
     * in readOnly mode
     */
    @Override
    public void setReadOnly(boolean readOnly) {
        if (!readOnlyFocusable) {
            super.setReadOnly(readOnly);
        } else {
            setTabIndex(0);
            getElement().setPropertyBoolean("readOnly", readOnly);
            String readOnlyStyle = "readonly";
            if (readOnly) {
                addStyleDependentName(readOnlyStyle);
            } else {
                removeStyleDependentName(readOnlyStyle);
            }
        }
    }

    public boolean isReadOnlyFocusable() {
        return readOnlyFocusable;
    }

    public void setReadOnlyFocusable(boolean readOnlyFocusable) {
        this.readOnlyFocusable = readOnlyFocusable;
    }
}