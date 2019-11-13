package com.haulmont.cuba.web.widgets;

import com.haulmont.cuba.web.widgets.grid.CubaEditorField;
import com.vaadin.data.HasValue;
import com.vaadin.ui.Grid;
import com.vaadin.ui.components.grid.GridSelectionModel;

import java.util.Map;
import java.util.function.Consumer;

public interface CubaEnhancedGrid<T> {

    void setGridSelectionModel(GridSelectionModel<T> model);

    Map<String, String> getColumnIds();

    void setColumnIds(Map<String, String> ids);

    void addColumnId(String column, String value);

    void removeColumnId(String column);

    void repaint();

    CubaGridEditorFieldFactory<T> getCubaEditorFieldFactory();

    void setCubaEditorFieldFactory(CubaGridEditorFieldFactory<T> editorFieldFactory);

    CubaEditorField<?> getColumnEditorField(T bean, Grid.Column<T, ?> column);

    void setBeforeRefreshHandler(Consumer<T> beforeRefreshHandler);

    void setShowEmptyState(boolean show);

    String getEmptyStateMessage();
    void setEmptyStateMessage(String message);

    String getEmptyStateLinkMessage();
    void setEmptyStateLinkMessage(String linkMessage);

    void setEmptyStateLinkClickHandler(Runnable handler);

    /**
     * CAUTION! Safari hides footer while changing predefined styles at runtime. Given method updates footer visibility
     * without changing its value.
     */
    void updateFooterVisibility();

    String getSelectAllLabel();
    void setSelectAllLabel(String selectAllLabel);

    String getDeselectAllLabel();
    void setDeselectAllLabel(String deselectAllLabel);
}
