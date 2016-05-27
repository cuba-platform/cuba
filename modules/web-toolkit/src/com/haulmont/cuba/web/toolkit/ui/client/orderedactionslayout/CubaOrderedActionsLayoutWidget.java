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

package com.haulmont.cuba.web.toolkit.ui.client.orderedactionslayout;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.StyleConstants;
import com.vaadin.client.ui.ShortcutActionHandler;
import com.vaadin.client.ui.orderedlayout.Slot;
import com.vaadin.client.ui.orderedlayout.VAbstractOrderedLayout;

public class CubaOrderedActionsLayoutWidget extends VAbstractOrderedLayout {
    protected ShortcutActionHandler shortcutHandler;

    public CubaOrderedActionsLayoutWidget() {
        super(false);
    }

    public CubaOrderedActionsLayoutWidget(String className, boolean vertical) {
        super(vertical);
        getElement().setTabIndex(-1);
        setStyleName(className);
        DOM.sinkEvents(getElement(), Event.ONKEYDOWN);
    }

    @Override
    public void setStyleName(String style) {
        super.setStyleName(style);
        addStyleName(StyleConstants.UI_LAYOUT);
        addStyleName(vertical ? "v-vertical" : "v-horizontal");
    }

    @Override
    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);

        final int type = DOM.eventGetType(event);
        if (type == Event.ONKEYDOWN && shortcutHandler != null) {
            shortcutHandler.handleKeyboardEvent(event);
        }
    }

    public ShortcutActionHandler getShortcutHandler() {
        return shortcutHandler;
    }

    public void setShortcutHandler(ShortcutActionHandler shortcutHandler) {
        this.shortcutHandler = shortcutHandler;
    }

    @Override
    public Slot getSlot(Widget widget) {
        Slot slot = widgetToSlot.get(widget);
        if (slot == null) {
            slot = new CubaOrderedLayoutSlot(this, widget);
            widgetToSlot.put(widget, slot);
        }
        return slot;
    }
}