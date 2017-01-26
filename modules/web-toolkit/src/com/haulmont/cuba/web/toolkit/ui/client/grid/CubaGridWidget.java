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
 */

package com.haulmont.cuba.web.toolkit.ui.client.grid;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.WidgetUtil;
import com.vaadin.client.widgets.Grid;
import elemental.json.JsonObject;

public class CubaGridWidget extends Grid<JsonObject> {

    @Override
    protected void sortWithSorter(Column<?, ?> column, boolean shiftKeyDown) {
        // ignore shiftKeyDown until datasources don't support multi-sorting
        super.sortWithSorter(column, false);
    }

    @Override
    protected void sortAfterDelayWithSorter(int delay, boolean multisort) {
        // ignore shiftKeyDown until datasources don't support multi-sorting
        super.sortAfterDelayWithSorter(delay, false);
    }

    @Override
    protected boolean isWidgetAllowsClickHandling(Element targetElement) {
        // by default, clicking on widget renderer prevents row selection
        // for some widget renderers we want to allow row selection
        return isClickThroughEnabled(targetElement);
    }

    @Override
    protected boolean isEventHandlerShouldHandleEvent(Element targetElement) {
        // by default, clicking on widget renderer prevents cell focus changing
        // for some widget renderers we want to allow focus changing
        return isClickThroughEnabled(targetElement);
    }

    protected boolean isClickThroughEnabled(Element e) {
        Widget widget = WidgetUtil.findWidget(e, null);
        return widget instanceof HasClickSettings &&
                ((HasClickSettings) widget).isClickThroughEnabled();
    }
}
