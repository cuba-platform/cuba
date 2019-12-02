package com.haulmont.cuba.web.widgets;

import com.haulmont.cuba.web.widgets.client.grid.CubaGridServerRpc;
import com.haulmont.cuba.web.widgets.client.grid.CubsGridClientRpc;
import com.haulmont.cuba.web.widgets.client.treegrid.CubaTreeGridState;
import com.haulmont.cuba.web.widgets.data.EnhancedHierarchicalDataProvider;
import com.haulmont.cuba.web.widgets.grid.CubaEditorField;
import com.haulmont.cuba.web.widgets.grid.CubaEditorImpl;
import com.haulmont.cuba.web.widgets.grid.CubaGridColumn;
import com.vaadin.data.ValueProvider;
import com.vaadin.data.provider.HierarchicalDataProvider;
import com.vaadin.ui.TreeGrid;
import com.vaadin.ui.components.grid.Editor;
import com.vaadin.ui.components.grid.GridSelectionModel;
import com.vaadin.ui.renderers.AbstractRenderer;

import java.util.*;
import java.util.function.Consumer;

public class CubaTreeGrid<T> extends TreeGrid<T> implements CubaEnhancedGrid<T> {

    protected CubaGridEditorFieldFactory<T> editorFieldFactory;

    protected Runnable emptyStateLinkClickHandler;

    protected boolean aggregatable = false;
    protected AggregationPosition aggregationPosition = AggregationPosition.TOP;
    protected Collection<String> aggregationPropertyIds;

    public CubaTreeGrid() {
        registerRpc((CubaGridServerRpc) () -> {
            if (emptyStateLinkClickHandler != null) {
                emptyStateLinkClickHandler.run();
            }
        });
    }

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

    @Override
    protected <V, P> Column<T, V> createColumn(ValueProvider<T, V> valueProvider,
                                               ValueProvider<V, P> presentationProvider,
                                               AbstractRenderer<? super T, ? super P> renderer) {
        return new CubaGridColumn<>(valueProvider, presentationProvider, renderer);
    }

    @Override
    public CubaGridEditorFieldFactory<T> getCubaEditorFieldFactory() {
        return editorFieldFactory;
    }

    @Override
    public void setCubaEditorFieldFactory(CubaGridEditorFieldFactory<T> editorFieldFactory) {
        this.editorFieldFactory = editorFieldFactory;
    }

    @Override
    protected Editor<T> createEditor() {
        return new CubaEditorImpl<>(getPropertySet());
    }

    @Override
    public CubaEditorField<?> getColumnEditorField(T bean, Column<T, ?> column) {
        return editorFieldFactory.createField(bean, column);
    }

    @SuppressWarnings("unchecked")
    public int getLevel(T item) {
        HierarchicalDataProvider<T, ?> dataProvider = getDataProvider();
        if (!(dataProvider instanceof EnhancedHierarchicalDataProvider)) {
            throw new IllegalStateException(
                    "Data provider must implement com.haulmont.cuba.web.widgets.data.EnhancedHierarchicalDataProvider"
            );
        }
        return ((EnhancedHierarchicalDataProvider<T>) dataProvider).getLevel(item);
    }

    public void expandItemWithParents(T item) {
        List<T> itemsToExpand = new ArrayList<>();

        T current = item;
        while (current != null) {
            itemsToExpand.add(current);
            current = getParentItem(current);
        }

        expand(itemsToExpand);
    }

    @SuppressWarnings("unchecked")
    protected T getParentItem(T item) {
        return ((EnhancedHierarchicalDataProvider<T>) getDataProvider()).getParent(item);
    }

    @Override
    public void setBeforeRefreshHandler(Consumer<T> beforeRefreshHandler) {
        getDataCommunicator().setBeforeRefreshHandler(beforeRefreshHandler);
    }

    @Override
    public void setShowEmptyState(boolean show) {
        if (getState(false).showEmptyState != show) {
            getState().showEmptyState = show;
        }
    }

    @Override
    public String getEmptyStateMessage() {
        return getState(false).emptyStateMessage;
    }

    @Override
    public void setEmptyStateMessage(String message) {
        getState().emptyStateMessage = message;
    }

    @Override
    public String getEmptyStateLinkMessage() {
        return getState(false).emptyStateLinkMessage;
    }

    @Override
    public void setEmptyStateLinkMessage(String linkMessage) {
        getState().emptyStateLinkMessage = linkMessage;
    }

    @Override
    public void setEmptyStateLinkClickHandler(Runnable handler) {
        this.emptyStateLinkClickHandler = handler;
    }

    @Override
    public void updateFooterVisibility() {
        getRpcProxy(CubsGridClientRpc.class).updateFooterVisibility();
    }

    @Override
    public String getSelectAllLabel() {
        return getState().selectAllLabel;
    }

    @Override
    public void setSelectAllLabel(String selectAllLabel) {
        getState(true).selectAllLabel = selectAllLabel;
    }

    @Override
    public String getDeselectAllLabel() {
        return getState().deselectAllLabel;
    }

    @Override
    public void setDeselectAllLabel(String deselectAllLabel) {
        getState(true).deselectAllLabel = deselectAllLabel;
    }


    @Override
    public boolean isAggregatable() {
        return aggregatable;
    }

    @Override
    public void setAggregatable(boolean aggregatable) {
        this.aggregatable = aggregatable;
    }

    @Override
    public AggregationPosition getAggregationPosition() {
        return aggregationPosition;
    }

    @Override
    public void setAggregationPosition(AggregationPosition position) {
        this.aggregationPosition = position;
    }

    @Override
    public Collection<String> getAggregationPropertyIds() {
        if (aggregationPropertyIds == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableCollection(aggregationPropertyIds);
    }

    @Override
    public void addAggregationPropertyId(String propertyId) {
        if (aggregationPropertyIds == null) {
            aggregationPropertyIds = new ArrayList<>();
        } else if (aggregationPropertyIds.contains(propertyId)) {
            throw new IllegalStateException(String.format("Aggregation property %s already exists", propertyId));
        }
        aggregationPropertyIds.add(propertyId);
    }

    @Override
    public void removeAggregationPropertyId(String propertyId) {
        if (aggregationPropertyIds != null) {
            aggregationPropertyIds.remove(propertyId);
            if (aggregationPropertyIds.isEmpty()) {
                aggregationPropertyIds = null;
            }
        }
    }
}