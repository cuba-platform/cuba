/*
 * Copyright (c) 2008-2017 Haulmont.
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
 */

package com.haulmont.cuba.web.widgets.client.grid.events;

import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.haulmont.cuba.web.widgets.client.grid.CubaGridWidget;
import com.vaadin.client.WidgetUtil;
import com.vaadin.v7.client.widget.grid.events.AbstractGridMouseEventHandler;
import com.vaadin.v7.client.widget.grid.events.GridDoubleClickEvent;
import com.vaadin.v7.client.widgets.Grid;

public class CubaGridDoubleClickEvent extends GridDoubleClickEvent {
    public static final Type<AbstractGridMouseEventHandler.GridDoubleClickHandler> EVENT_TYPE =
            new Type<>(BrowserEvents.DBLCLICK, new CubaGridDoubleClickEvent());

    @Override
    public Type<AbstractGridMouseEventHandler.GridDoubleClickHandler> getAssociatedType() {
        return EVENT_TYPE;
    }

    @Override
    public Grid<?> getGrid() {
        EventTarget target = getNativeEvent().getEventTarget();
        if (!Element.is(target)) {
            return null;
        }
        return WidgetUtil.findWidget(Element.as(target), CubaGridWidget.class);
    }
}
