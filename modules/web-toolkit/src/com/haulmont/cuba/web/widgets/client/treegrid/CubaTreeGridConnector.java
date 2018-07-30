package com.haulmont.cuba.web.widgets.client.treegrid;

import com.haulmont.cuba.web.widgets.CubaTreeGrid;
import com.vaadin.client.ui.treegrid.TreeGridConnector;
import com.vaadin.client.widgets.Grid;
import com.vaadin.shared.ui.Connect;
import elemental.json.JsonObject;

import java.util.List;

@Connect(CubaTreeGrid.class)
public class CubaTreeGridConnector extends TreeGridConnector {

    @Override
    public CubaTreeGridWidget getWidget() {
        return (CubaTreeGridWidget) super.getWidget();
    }

    @Override
    public CubaTreeGridState getState() {
        return (CubaTreeGridState) super.getState();
    }

    @Override
    protected void updateColumns() {
        super.updateColumns();

        if (getWidget().getColumnIds() != null) {
            getWidget().setColumnIds(null);
        }

        if (getState().columnIds != null) {
            List<Grid.Column<?, JsonObject>> currentColumns = getWidget().getColumns();

            for (Grid.Column<?, JsonObject> column : currentColumns) {
                String id = getColumnId(column);
                if (getState().columnIds.containsKey(id)) {
                    getWidget().addColumnId(column, getState().columnIds.get(id));
                }
            }
        }
    }
}
