package com.haulmont.cuba.web.widgets;

import com.haulmont.cuba.web.widgets.grid.CubaEditorField;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Grid;
import com.vaadin.ui.components.grid.GridSelectionModel;

import javax.annotation.Nullable;
import java.util.Collection;
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

    boolean isAggregatable();

    void setAggregatable(boolean aggregatable);

    AggregationPosition getAggregationPosition();

    void setAggregationPosition(AggregationPosition position);

    void addAggregationPropertyId(String propertyId);

    void removeAggregationPropertyId(String propertyId);

    Collection<String> getAggregationPropertyIds();

    ContentMode getRowDescriptionContentMode();

    @Nullable
    Float getMinHeight();

    @Nullable
    Sizeable.Unit getMinHeightSizeUnit();

    void setMinHeight(@Nullable String minHeight);

    @Nullable
    Float getMinWidth();

    @Nullable
    Sizeable.Unit getMinWidthSizeUnit();

    void setMinWidth(@Nullable String minWidth);

    /**
     * Defines the position of aggregation row.
     */
    enum AggregationPosition {
        TOP,
        BOTTOM
    }
}
