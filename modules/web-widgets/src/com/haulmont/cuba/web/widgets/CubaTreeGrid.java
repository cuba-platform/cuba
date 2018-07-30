package com.haulmont.cuba.web.widgets;

import com.haulmont.cuba.web.widgets.client.treegrid.CubaTreeGridState;
import com.vaadin.ui.TreeGrid;
import com.vaadin.ui.components.grid.GridSelectionModel;

import java.util.HashMap;
import java.util.Map;

public class CubaTreeGrid<T> extends TreeGrid<T> implements CubaEnhancedGrid<T> {

    @Override
    public void setGridSelectionModel(GridSelectionModel<T> model) {
        setSelectionModel(model);
    }

    @Override
    protected CubaTreeGridState getState() {
        return (CubaTreeGridState) super.getState();
    }

    @Override
    protected CubaTreeGridState getState(boolean markAsDirty) {
        return (CubaTreeGridState) super.getState(markAsDirty);
    }

    @Override
    public Map<String, String> getColumnIds() {
        return getState().columnIds;
    }

    @Override
    public void setColumnIds(Map<String, String> ids) {
        getState().columnIds = ids;
    }

    @Override
    public void addColumnId(String column, String value) {
        if (getState().columnIds == null) {
            getState().columnIds = new HashMap<>();
        }

        getState().columnIds.put(column, value);
    }

    @Override
    public void removeColumnId(String column) {
        if (getState().columnIds != null) {
            getState().columnIds.remove(column);
        }
    }

    @Override
    public void repaint() {
        markAsDirtyRecursive();
        getDataCommunicator().reset();
    }
}
