package com.haulmont.cuba.web.widgets.client.treegrid;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Widget;
import com.haulmont.cuba.web.widgets.client.grid.HasClickSettings;
import com.vaadin.client.WidgetUtil;
import com.vaadin.client.renderers.Renderer;
import com.vaadin.client.widget.escalator.FlyweightCell;
import com.vaadin.client.widget.escalator.RowContainer;
import com.vaadin.client.widget.grid.events.GridClickEvent;
import com.vaadin.client.widget.treegrid.TreeGrid;
import elemental.json.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class CubaTreeGridWidget extends TreeGrid {

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

//    @Override
//    protected Editor<JsonObject> createEditor() {
//        Editor<JsonObject> editor = super.createEditor();
//        editor.setEventHandler(new CubaEditorEventHandler<>());
//        return editor;
//    }
//

    @Override
    protected boolean isWidgetAllowsClickHandling(Element targetElement) {
        // by default, clicking on widget renderer prevents row selection
        // we want to allow row selection
        return true;
    }

    @Override
    protected boolean isEventHandlerShouldHandleEvent(Element targetElement) {
        // TEST: gg, instanceof is used for the ComponentRenderer. Check if we need some changes in the renderer
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
    protected UserSorter createUserSorter() {
        return new CubaTreeGridWidget.CubaUserSorter();
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
    }

    @Override
    protected ColumnHider createColumnHider() {
        return new CubaTreeGridWidget.CubaColumnHider();
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

    @Override
    protected SelectionColumn createSelectionColumn(Renderer<Boolean> selectColumnRenderer) {
        return new CubaTreeGridWidget.CubaSelectionColumn(selectColumnRenderer);
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
}
