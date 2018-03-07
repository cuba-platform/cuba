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

package com.haulmont.cuba.web.widgets.client.grid;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import com.haulmont.cuba.web.widgets.client.grid.events.CubaGridClickEvent;
import com.haulmont.cuba.web.widgets.client.grid.events.CubaGridDoubleClickEvent;
import com.haulmont.cuba.web.widgets.client.grid.events.CubaGridKeyDownEvent;
import com.haulmont.cuba.web.widgets.client.grid.events.CubaGridKeyPressEvent;
import com.haulmont.cuba.web.widgets.client.grid.events.CubaGridKeyUpEvent;
import com.vaadin.client.WidgetUtil;
import com.vaadin.v7.client.renderers.Renderer;
import com.vaadin.v7.client.widget.escalator.EscalatorUpdater;
import com.vaadin.v7.client.widget.escalator.FlyweightCell;
import com.vaadin.v7.client.widget.escalator.RowContainer;
import com.vaadin.v7.client.widget.grid.events.BodyClickHandler;
import com.vaadin.v7.client.widget.grid.events.BodyDoubleClickHandler;
import com.vaadin.v7.client.widget.grid.events.BodyKeyDownHandler;
import com.vaadin.v7.client.widget.grid.events.BodyKeyPressHandler;
import com.vaadin.v7.client.widget.grid.events.BodyKeyUpHandler;
import com.vaadin.v7.client.widget.grid.events.FooterClickHandler;
import com.vaadin.v7.client.widget.grid.events.FooterDoubleClickHandler;
import com.vaadin.v7.client.widget.grid.events.FooterKeyDownHandler;
import com.vaadin.v7.client.widget.grid.events.FooterKeyPressHandler;
import com.vaadin.v7.client.widget.grid.events.FooterKeyUpHandler;
import com.vaadin.v7.client.widget.grid.events.HeaderClickHandler;
import com.vaadin.v7.client.widget.grid.events.HeaderDoubleClickHandler;
import com.vaadin.v7.client.widget.grid.events.HeaderKeyDownHandler;
import com.vaadin.v7.client.widget.grid.events.HeaderKeyPressHandler;
import com.vaadin.v7.client.widget.grid.events.HeaderKeyUpHandler;
import com.vaadin.v7.client.widgets.Grid;
import elemental.json.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class CubaGridWidget extends Grid<JsonObject> {

    public static final String CUBA_ID_COLUMN_PREFIX = "column_";
    public static final String CUBA_ID_COLUMN_HIDING_TOGGLE_PREFIX = "cc_";

    protected Map<Column<?, JsonObject>, String> columnIds = null;

    public Map<Column<?, JsonObject>, String> getColumnIds() {
        return columnIds;
    }

    public void setColumnIds(Map<Column<?, JsonObject>, String> columnProperties) {
        this.columnIds = columnProperties;
    }

    public void addColumnId(Column<?, JsonObject> column, String id) {
        if (columnIds == null) {
            columnIds = new HashMap<>();
        }

        columnIds.put(column, id);
    }

    public void removeColumnId(Column<?, JsonObject> column) {
        if (columnIds != null) {
            columnIds.remove(column);
        }
    }

    @Override
    protected Editor<JsonObject> createEditor() {
        Editor<JsonObject> editor = super.createEditor();
        editor.setEventHandler(new CubaEditorEventHandler<>());
        return editor;
    }

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
        // we want to allow row selection
        return true;
    }

    @Override
    protected boolean isEventHandlerShouldHandleEvent(Element targetElement) {
        // by default, clicking on widget renderer prevents cell focus changing
        // for some widget renderers we want to allow focus changing
        Widget widget = WidgetUtil.findWidget(targetElement, null);
        return !(widget instanceof com.vaadin.client.Focusable
                || widget instanceof com.google.gwt.user.client.ui.Focusable)
                || isClickThroughEnabled(targetElement);
    }

    protected boolean isClickThroughEnabled(Element e) {
        Widget widget = WidgetUtil.findWidget(e, null);
        return widget instanceof HasClickSettings &&
                ((HasClickSettings) widget).isClickThroughEnabled();
    }

    @Override
    public HandlerRegistration addBodyKeyDownHandler(BodyKeyDownHandler handler) {
        return addHandler(handler, CubaGridKeyDownEvent.EVENT_TYPE);
    }

    @Override
    public HandlerRegistration addBodyKeyUpHandler(BodyKeyUpHandler handler) {
        return addHandler(handler, CubaGridKeyUpEvent.EVENT_TYPE);
    }

    @Override
    public HandlerRegistration addBodyKeyPressHandler(BodyKeyPressHandler handler) {
        return addHandler(handler, CubaGridKeyPressEvent.EVENT_TYPE);
    }

    @Override
    public HandlerRegistration addHeaderKeyDownHandler(HeaderKeyDownHandler handler) {
        return addHandler(handler, CubaGridKeyDownEvent.EVENT_TYPE);
    }

    @Override
    public HandlerRegistration addHeaderKeyUpHandler(HeaderKeyUpHandler handler) {
        return addHandler(handler, CubaGridKeyUpEvent.EVENT_TYPE);
    }

    @Override
    public HandlerRegistration addHeaderKeyPressHandler(HeaderKeyPressHandler handler) {
        return addHandler(handler, CubaGridKeyPressEvent.EVENT_TYPE);
    }

    @Override
    public HandlerRegistration addFooterKeyDownHandler(FooterKeyDownHandler handler) {
        return addHandler(handler, CubaGridKeyDownEvent.EVENT_TYPE);
    }

    @Override
    public HandlerRegistration addFooterKeyUpHandler(FooterKeyUpHandler handler) {
        return addHandler(handler, CubaGridKeyUpEvent.EVENT_TYPE);
    }

    @Override
    public HandlerRegistration addFooterKeyPressHandler(FooterKeyPressHandler handler) {
        return addHandler(handler, CubaGridKeyPressEvent.EVENT_TYPE);
    }

    @Override
    public HandlerRegistration addBodyClickHandler(BodyClickHandler handler) {
        return addHandler(handler, CubaGridClickEvent.EVENT_TYPE);
    }

    @Override
    public HandlerRegistration addHeaderClickHandler(HeaderClickHandler handler) {
        return addHandler(handler, CubaGridClickEvent.EVENT_TYPE);
    }

    @Override
    public HandlerRegistration addFooterClickHandler(FooterClickHandler handler) {
        return addHandler(handler, CubaGridClickEvent.EVENT_TYPE);
    }

    @Override
    public HandlerRegistration addBodyDoubleClickHandler(BodyDoubleClickHandler handler) {
        return addHandler(handler, CubaGridDoubleClickEvent.EVENT_TYPE);
    }

    @Override
    public HandlerRegistration addHeaderDoubleClickHandler(HeaderDoubleClickHandler handler) {
        return addHandler(handler, CubaGridDoubleClickEvent.EVENT_TYPE);
    }

    @Override
    public HandlerRegistration addFooterDoubleClickHandler(FooterDoubleClickHandler handler) {
        return addHandler(handler, CubaGridDoubleClickEvent.EVENT_TYPE);
    }

    @Override
    protected EscalatorUpdater createHeaderUpdater() {
        return new CubaStaticSectionUpdater(getHeader(), getEscalator().getHeader());
    }

    @Override
    protected EscalatorUpdater createFooterUpdater() {
        return new CubaStaticSectionUpdater(getFooter(), getEscalator().getFooter());
    }

    protected class CubaStaticSectionUpdater extends StaticSectionUpdater {

        public CubaStaticSectionUpdater(StaticSection<?> section, RowContainer container) {
            super(section, container);
        }

        @Override
        protected void addAdditionalData(StaticSection.StaticRow<?> staticRow, FlyweightCell cell) {
            if (columnIds != null) {
                Column<?, JsonObject> column = getVisibleColumns().get(cell.getColumn());
                Object columnId = (columnIds.containsKey(column))
                        ? columnIds.get(column)
                        : cell.getColumn();

                Element cellElement = cell.getElement();
                cellElement.setAttribute("cuba-id", CUBA_ID_COLUMN_PREFIX + columnId);
            }
        }
    }

    @Override
    protected ColumnHider createColumnHider() {
        return new CubaColumnHider();
    }

    protected class CubaColumnHider extends ColumnHider {
        @Override
        protected String getCustomHtmlAttributes(Column<?, JsonObject> column) {
            if (columnIds != null) {
                Object columnId = (columnIds.get(column));
                if (columnId != null) {
                    return "cuba-id=\"" +
                            CUBA_ID_COLUMN_HIDING_TOGGLE_PREFIX +
                            CUBA_ID_COLUMN_PREFIX +
                            columnId + "\"";
                }
            }

            return super.getCustomHtmlAttributes(column);
        }
    }

    /* todo vaadin8
    @Override
    protected SelectionColumn createSelectionColumn(Renderer<Boolean> selectColumnRenderer) {
        return new CubaSelectionColumn(selectColumnRenderer);
    }

    protected class CubaSelectionColumn extends SelectionColumn {

        public CubaSelectionColumn(Renderer<Boolean> selectColumnRenderer) {
            super(selectColumnRenderer);
        }

        @Override
        protected HeaderClickHandler createHeaderClickHandler() {
            return event -> {
                // do nothing, as we want trigger select/deselect all only by clicking on the checkbox
            };
        }
    }*/
}
