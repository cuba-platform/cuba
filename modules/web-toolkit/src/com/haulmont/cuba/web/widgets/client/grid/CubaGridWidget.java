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

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.*;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.Widget;
import com.haulmont.cuba.web.widgets.client.Tools;
import com.haulmont.cuba.web.widgets.client.grid.events.ColumnFilterClickEvent;
import com.haulmont.cuba.web.widgets.client.grid.events.ColumnFilterClickHandler;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.Focusable;
import com.vaadin.client.WidgetUtil;
import com.vaadin.client.renderers.Renderer;
import com.vaadin.client.ui.VOverlay;
import com.vaadin.client.widget.escalator.EscalatorUpdater;
import com.vaadin.client.widget.escalator.FlyweightCell;
import com.vaadin.client.widget.escalator.RowContainer;
import com.vaadin.client.widget.grid.AutoScroller;
import com.vaadin.client.widget.grid.events.GridClickEvent;
import com.vaadin.client.widget.grid.selection.SelectionModel;
import com.vaadin.client.widgets.Escalator;
import com.vaadin.client.widgets.Grid;
import com.vaadin.shared.Connector;
import com.vaadin.shared.ui.grid.HeightMode;
import elemental.events.Event;
import elemental.json.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class CubaGridWidget extends Grid<JsonObject> {

    public static final String CUBA_ID_COLUMN_PREFIX = "column_";
    public static final String CUBA_ID_COLUMN_HIDING_TOGGLE_PREFIX = "cc_";
    public static final String LAST_COLUMN_STYLENAME = "c-last-column";
    public static final String COLUMN_HIDING_TOGGLE_STYLENAME = "column-hiding-toggle";
    public static final String GRID_COLUMN_FILTER_STYLENAME = "c-grid-column-filter";
    public static final String CELL_HAS_FILTER_STYLENAME = "c-cell-has-filter";

    protected Map<Column<?, JsonObject>, String> columnIds = null;

    protected CubaGridEmptyState emptyState;
    protected Runnable emptyStateLinkClickHandler;

    protected String selectAllLabel;
    protected String deselectAllLabel;

    protected Widget columnFilterPopup;

    public CubaGridWidget() {
        super();

        addBrowserEventHandler(0, createCubaColumnFilterEventHandler());
    }

    protected CubaColumnFilterEventHandler createCubaColumnFilterEventHandler() {
        return new CubaColumnFilterEventHandler();
    }

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

    public String getSelectAllLabel() {
        return selectAllLabel;
    }

    public void setSelectAllLabel(String selectAllLabel) {
        this.selectAllLabel = selectAllLabel;
    }

    public String getDeselectAllLabel() {
        return deselectAllLabel;
    }

    public void setDeselectAllLabel(String deselectAllLabel) {
        this.deselectAllLabel = deselectAllLabel;
    }

    /*
     * Workaround to avoid disappearing footer when changing the predefined styles at runtime in Safari
     */
    public void updateFooterVisibility() {
        Footer footer = getFooter();
        if (!footer.isVisible()) {
            return;
        }

        footer.setVisible(false);
        footer.setVisible(true);
    }

    public void showEmptyState(boolean show) {
        if (show) {
            if (emptyState == null) {
                emptyState = new CubaGridEmptyState();
            }

            Element wrapper = getEscalator().getTableWrapper();
            Element panelParent = emptyState.getElement().getParentElement();

            if (panelParent == null || !panelParent.equals(wrapper)) {
                wrapper.appendChild(emptyState.getElement());
            }
        } else if (emptyState != null) {
            emptyState.getElement().removeFromParent();
            emptyState = null;
        }
    }

    public CubaGridEmptyState getEmptyState() {
        return emptyState;
    }

    @Override
    protected Editor<JsonObject> createEditor() {
        Editor<JsonObject> editor = super.createEditor();
        editor.setEventHandler(new CubaEditorEventHandler<>());
        return editor;
    }

    @Override
    protected boolean isWidgetAllowsClickHandling(Element targetElement, NativeEvent nativeEvent) {
        // By default, clicking on widget renderer prevents row selection.
        // We want to allow row selection. Every time selection is changed,
        // all renderers render their content, as the result, components rendered by
        // ComponentRenderer lose focus because they are replaced with new instances,
        // so we prevent click handling for Focus widgets.
        Widget widget = WidgetUtil.findWidget(targetElement, null);
        return !isWidgetOrParentFocusable(widget);
    }

    protected boolean isWidgetOrParentFocusable(Widget widget) {
        boolean widgetFocusable = isWidgetFocusable(widget);
        if (!widgetFocusable) {
            Widget parent = widget.getParent();
            while (parent != null
                    && !widgetFocusable
                    && !isGridCell(parent)) {
                widgetFocusable = isWidgetFocusable(parent);
                parent = parent.getParent();
            }
        }
        return widgetFocusable;
    }

    private boolean isGridCell(Widget parent) {
        String styleName = parent.getStyleName();
        // We assume that in most cases Widget is added by a ComponentRenderer,
        // so it's wrapped by a div with the 'component-wrap' style name.
        // If for some reason we didn't find a component wrapper, we stop when we reached a grid.
        return styleName != null && styleName.contains("component-wrap")
                || parent instanceof CubaGridWidget;
    }

    protected boolean isWidgetFocusable(Widget widget) {
        return widget instanceof Focusable
                || widget instanceof com.google.gwt.user.client.ui.Focusable;
    }

    @Override
    protected boolean isEventHandlerShouldHandleEvent(Element targetElement, GridEvent<JsonObject> event) {
        if (!event.getDomEvent().getType().equals(Event.MOUSEDOWN)
                && !event.getDomEvent().getType().equals(Event.CLICK)) {
            return super.isEventHandlerShouldHandleEvent(targetElement, event);
        }

        // By default, clicking on widget renderer prevents cell focus changing
        // for some widget renderers we want to allow focus changing
        Widget widget = WidgetUtil.findWidget(targetElement, null);
        return !(isWidgetOrParentFocusable(widget))
                || isClickThroughEnabled(targetElement);
    }

    protected boolean isClickThroughEnabled(Element e) {
        Widget widget = WidgetUtil.findWidget(e, null);
        return widget instanceof HasClickSettings &&
                ((HasClickSettings) widget).isClickThroughEnabled();
    }

    @Override
    protected EscalatorUpdater createHeaderUpdater() {
        return new CubaStaticSectionUpdater(getHeader(), getEscalator().getHeader());
    }

    @Override
    protected EscalatorUpdater createFooterUpdater() {
        return new CubaStaticSectionUpdater(getFooter(), getEscalator().getFooter());
    }

    @Override
    protected UserSorter createUserSorter() {
        return new CubaUserSorter();
    }

    protected class CubaUserSorter extends UserSorter {

        protected CubaUserSorter() {
        }

        @Override
        public void sort(Column<?, ?> column, boolean multisort) {
            // ignore 'multisort' until datasources don't support multi-sorting
            super.sort(column, false);
        }
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

        @Override
        protected void addAdditionalElementsAndStyles(StaticSection.StaticRow<?> staticRow,
                                                      FlyweightCell cell) {
            // if the last column, SidebarMenu is visible and no vertical scroll
            if (cell.getColumn() == getVisibleColumns().size() - 1
                    && getSidebar().getParent() != null
                    && isHeaderDecoHidden()) {
                TableCellElement cellElement = cell.getElement();
                cellElement.addClassName(LAST_COLUMN_STYLENAME);
            }

            if (staticRow instanceof HeaderRow
                    && ((HeaderRow) staticRow).isDefault()) {
                Element filterElement = DOM.createDiv();
                filterElement.addClassName(GRID_COLUMN_FILTER_STYLENAME);

                Element td = cell.getElement();
                td.appendChild(filterElement);
                td.addClassName(CELL_HAS_FILTER_STYLENAME);
            }
        }

        protected boolean isHeaderDecoHidden() {
            DivElement headerDeco = getGrid().getEscalator().getHeaderDeco();
            Style style = headerDeco.getStyle();

            return Style.Display.NONE.getCssName().equals(style.getDisplay())
                    || getEscalator().getVerticalScrollbar().isInvisibleScrollbar();
        }

        @Override
        protected void cleanup(FlyweightCell cell) {
            super.cleanup(cell);
            cell.getElement().removeClassName(CELL_HAS_FILTER_STYLENAME);
            cell.getElement().removeClassName(LAST_COLUMN_STYLENAME);
        }
    }

    public HandlerRegistration addColumnFilterClickHandler(ColumnFilterClickHandler<JsonObject> handler) {
        return addHandler(handler, ColumnFilterClickEvent.getType());
    }

    public void setColumnFilterPopup(Widget columnFilterPopup) {
        this.columnFilterPopup = columnFilterPopup;
    }

    public Widget getColumnFilterPopup() {
        return columnFilterPopup;
    }

    public void showColumnFilterPopup(int clientX, int clientY) {
        if (columnFilterPopup == null) {
            return;
        }

        VOverlay popupOverlay = new VOverlay();
        popupOverlay.setOwner(this);
        popupOverlay.setWidget(columnFilterPopup);

        popupOverlay.addCloseHandler(closeEvent -> {

//            popupOverlay = null;
        });

        Tools.showPopup(popupOverlay, clientX, clientY);
    }

    protected class CubaColumnFilterEventHandler extends AbstractGridEventHandler {
        @Override
        public void onEvent(GridEvent<JsonObject> event) {
            super.onEvent(event);

            if (event.isHandled()) {
                return;
            }
            if (!event.getCell().isHeader()) {
                return;
            }
            if (!getHeader().getRow(event.getCell().getRowIndex())
                    .isDefault()) {
                return;
            }

            // TODO: gg, check that a column is filterable

            // TODO: gg, handle touch events?

            if (BrowserEvents.CLICK
                    .equals(event.getDomEvent().getType())) {

                EventTarget eventTarget = event.getDomEvent().getEventTarget();
                if (isFilterElement(eventTarget)) {
                    Element target = eventTarget.cast();

                    WidgetUtil.TextRectangle rect = WidgetUtil.getBoundingClientRect(target);
                    int clientX = (int) Math.ceil(rect.getLeft());
                    int clientY = (int) Math.ceil(rect.getBottom());

                    fireEvent(new ColumnFilterClickEvent<>(CubaGridWidget.this, event.getCell(),
                            clientX, clientY, true));

                    event.setHandled(true);
                }
            }
        }

        protected boolean isFilterElement(EventTarget eventTarget) {
            if (Element.is(eventTarget)) {
                Element target = eventTarget.cast();
                return GRID_COLUMN_FILTER_STYLENAME.equals(target.getClassName());
            }
            return false;
        }
    }

    @Override
    protected Sidebar createSidebar() {
        return new CubaSidebar(this);
    }

    protected static class CubaSidebar extends Sidebar {

        public CubaSidebar(CubaGridWidget grid) {
            super(grid);
        }

        @Override
        protected void updateVisibility() {
            super.updateVisibility();

            RowContainer header = getGrid().getEscalator().getHeader();
            if (header.getRowCount() > 0) {
                header.refreshRows(0, header.getRowCount());
            }
        }
    }

    @Override
    protected Escalator createEscalator() {
        return GWT.create(CubaEscalator.class);
    }

    public static class CubaEscalator extends Escalator {

        public CubaEscalator() {
            super();
        }

        @Override
        protected Scroller createScroller() {
            return new CubaScroller();
        }

        protected class CubaScroller extends Scroller {
            @Override
            protected void afterRecalculateScrollbarsForVirtualViewport() {
                RowContainer header = getHeader();
                if (header.getRowCount() > 0) {
                    header.refreshRows(0, header.getRowCount());
                }
            }
        }

        @Override
        protected double recalculateHeightOfEscalator() {
            double heightOfEscalator = super.recalculateHeightOfEscalator();
            if (getHeightMode() == HeightMode.UNDEFINED) {
                // In case of HeightMode.UNDEFINED we miss 1px, as the result:
                // 1. if no rows then the Sidebar button is bigger than header row
                // 2. if there are rows then the last row has the focus border cropped
                heightOfEscalator += 1;
            }
            return heightOfEscalator;
        }
    }

    @Override
    protected ColumnHider createColumnHider() {
        return new CubaColumnHider();
    }

    protected class CubaColumnHider extends ColumnHider {

        protected boolean defaultTogglesInitialized;

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

        @Override
        protected int getFirstColumnToggleIndex() {
            // Column toggles will be displayed after default toggles (selectAll + deselectAll + separator)
            return 3;
        }

        @Override
        protected void updateTogglesOrder() {
            if (!defaultTogglesInitialized) {
                defaultTogglesInitialized = true;
                initDefaultToggles();
            }

            super.updateTogglesOrder();
        }

        protected void initDefaultToggles() {
            MenuBar sidebarMenu = getSidebarMenu();

            sidebarMenu.addItem(createSelectAllToggle());
            sidebarMenu.addItem(createDeselectAllToggle());

            sidebarMenu.addSeparator();
        }

        protected MenuItem createSelectAllToggle() {
            MenuItem selectAllToggle = new MenuItem(getDefaultToggleHTML(getSelectAllLabel()), true, getSelectAllCommand());
            selectAllToggle.addStyleName(COLUMN_HIDING_TOGGLE_STYLENAME);
            return selectAllToggle;
        }

        protected Scheduler.ScheduledCommand getSelectAllCommand() {
            return () -> {
                for (Column column : getColumns()) {
                    if (column.isHidden()) {
                        column.setHidden(false);
                    }
                }
            };
        }

        protected MenuItem createDeselectAllToggle() {
            MenuItem deselectAllToggle = new MenuItem(getDefaultToggleHTML(getDeselectAllLabel()), true, getDeselectAllCommand());
            deselectAllToggle.addStyleName(COLUMN_HIDING_TOGGLE_STYLENAME);
            return deselectAllToggle;
        }

        protected Scheduler.ScheduledCommand getDeselectAllCommand() {
            return () -> {
                for (Column column : getColumns()) {
                    if (column.isHidable() && !column.isHidden()) {
                        column.setHidden(true);
                    }
                }
            };
        }

        protected String getDefaultToggleHTML(String caption) {
            return "<span class=\"v-off\">" +
                    "<div>" +
                    SafeHtmlUtils.htmlEscape(caption) +
                    "</div>" +
                    "</span>";
        }
    }

    @Override
    protected SelectionColumn createSelectionColumn(Renderer<Boolean> selectColumnRenderer) {
        return new CubaSelectionColumn(selectColumnRenderer);
    }

    protected class CubaSelectionColumn extends SelectionColumn {

        public CubaSelectionColumn(Renderer<Boolean> selectColumnRenderer) {
            super(selectColumnRenderer);
        }

        @Override
        protected void onHeaderClickEvent(GridClickEvent event) {
            // do nothing, as we want to trigger select/deselect all only by clicking on the checkbox
        }
    }

    @Override
    protected boolean hasSelectionColumn(SelectionModel<JsonObject> selectionModel) {
        return super.hasSelectionColumn(selectionModel)
                && getSelectionColumn().isPresent();
    }

    @Override
    protected AutoScroller createAutoScroller() {
        return new CubaAutoScroller(this);
    }

    public static class CubaAutoScroller extends AutoScroller {

        /**
         * Creates a new instance for scrolling the given grid.
         *
         * @param grid the grid to auto scroll
         */
        public CubaAutoScroller(Grid<?> grid) {
            super(grid);
        }

        @Override
        protected boolean hasSelectionColumn() {
            return super.hasSelectionColumn()
                    && grid.getSelectionColumn().isPresent();
        }
    }
}
