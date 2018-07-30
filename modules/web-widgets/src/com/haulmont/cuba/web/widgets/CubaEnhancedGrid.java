package com.haulmont.cuba.web.widgets;

import com.vaadin.ui.components.grid.GridSelectionModel;

import java.util.Map;

public interface CubaEnhancedGrid<T> {

    void setGridSelectionModel(GridSelectionModel<T> model);

    Map<String, String> getColumnIds();

    void setColumnIds(Map<String, String> ids);

    void addColumnId(String column, String value);

    void removeColumnId(String column);

    void repaint();
}
