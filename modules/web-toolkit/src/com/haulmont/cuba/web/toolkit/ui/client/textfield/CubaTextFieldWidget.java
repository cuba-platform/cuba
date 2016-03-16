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

package com.haulmont.cuba.web.toolkit.ui.client.textfield;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ui.ShortcutActionHandler;
import com.vaadin.client.ui.VTextField;

import java.util.Iterator;
import java.util.LinkedList;

/**
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