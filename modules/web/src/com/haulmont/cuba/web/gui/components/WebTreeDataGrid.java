package com.haulmont.cuba.web.gui.components;

import com.haulmont.bali.events.Subscription;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.TreeDataGrid;
import com.haulmont.cuba.gui.components.data.DataGridSource;
import com.haulmont.cuba.gui.components.data.TreeDataGridSource;
import com.haulmont.cuba.web.gui.components.datagrid.DataGridDataProvider;
import com.haulmont.cuba.web.gui.components.datagrid.HierarchicalDataGridDataProvider;
import com.haulmont.cuba.web.widgets.CubaTreeGrid;
import com.vaadin.data.TreeData;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.TreeDataProvider;
import com.vaadin.shared.Registration;
import com.vaadin.ui.Grid;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

public class WebTreeDataGrid<E extends Entity> extends WebAbstractDataGrid<CubaTreeGrid<E>, E>
        implements TreeDataGrid<E> {

    protected Predicate<E> itemCollapseAllowedProvider = t -> true;

    protected Registration expandListener;
    protected Registration collapseListener;

    @Override
    protected CubaTreeGrid<E> createComponent() {
        return new CubaTreeGrid<>();
    }

    @Nullable
    @Override
    public TreeDataGridSource<E> getDataGridSource() {
        return (TreeDataGridSource<E>) super.getDataGridSource();
    }

    @Override
    public void setDataGridSource(DataGridSource<E> dataGridSource) {
        if (dataGridSource != null
                && !(dataGridSource instanceof TreeDataGridSource)) {
            throw new IllegalArgumentException("TreeDataGrid supports only TreeDataGridSource data binding");
        }

        super.setDataGridSource(dataGridSource);
    }

    @Override
    protected DataProvider<E, ?> createEmptyDataProvider() {
        return new TreeDataProvider<>(new TreeData<>());
    }

    @Override
    protected void initComponent(Grid<E> component) {
        super.initComponent(component);

        CubaTreeGrid<E> treeGrid = (CubaTreeGrid<E>) component;
        treeGrid.setItemCollapseAllowedProvider(itemCollapseAllowedProvider::test);
    }

    @Override
    protected DataGridDataProvider<E> createDataGridDataProvider(DataGridSource<E> dataGridSource) {
        return new HierarchicalDataGridDataProvider<>((TreeDataGridSource<E>) dataGridSource, this);
    }

    @Override
    public Predicate<E> getItemCollapseAllowedProvider() {
        return itemCollapseAllowedProvider;
    }

    @Override
    public void setItemCollapseAllowedProvider(Predicate<E> provider) {
        checkNotNullArgument(provider);

        this.itemCollapseAllowedProvider = provider;
        // We reset a provider to the component in order to trigger the data update
        component.setItemCollapseAllowedProvider(provider::test);
    }

    @Nullable
    @Override
    public Column<E> getHierarchyColumn() {
        Grid.Column<E, ?> hierarchyColumn = component.getHierarchyColumn();
        return hierarchyColumn != null ? getColumnByGridColumn(hierarchyColumn) : null;
    }

    @Override
    public void setHierarchyColumn(String id) {
        setHierarchyColumn(getColumnNN(id));
    }

    @Override
    public void setHierarchyColumn(Column<E> column) {
        checkNotNullArgument(column);

        Grid.Column<E, ?> gridColumn = ((ColumnImpl<E>) column).getGridColumn();
        component.setHierarchyColumn(gridColumn);
    }

    @Override
    public void expand(Collection<E> items) {
        component.expand(items);
    }

    @Override
    public void expandRecursively(Stream<E> items, int depth) {
        component.expandRecursively(items, depth);
    }

    @Override
    public void expandAll() {
        if (getDataGridSource() != null) {
            expandRecursively(getDataGridSource().getChildren(null), Integer.MAX_VALUE);
        }
    }

    @Override
    public void collapse(Collection<E> items) {
        component.collapse(items);
    }

    @Override
    public void collapseRecursively(Stream<E> items, int depth) {
        component.collapseRecursively(items, depth);
    }

    @Override
    public void collapseAll() {
        if (getDataGridSource() != null) {
            collapseRecursively(getDataGridSource().getChildren(null), Integer.MAX_VALUE);
        }
    }

    @Override
    public boolean isExpanded(E item) {
        return component.isExpanded(item);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Subscription addExpandListener(Consumer<ExpandEvent<E>> listener) {
        if (expandListener == null) {
            expandListener = component.addExpandListener(this::onItemExpand);
        }

        return getEventHub().subscribe(ExpandEvent.class, (Consumer) listener);
    }

    protected void onItemExpand(com.vaadin.event.ExpandEvent<E> e) {
        ExpandEvent<E> event = new ExpandEvent<>(WebTreeDataGrid.this,
                e.getExpandedItem(), e.isUserOriginated());
        publish(ExpandEvent.class, event);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Subscription addCollapseListener(Consumer<CollapseEvent<E>> listener) {
        if (collapseListener == null) {
            collapseListener = component.addCollapseListener(this::onItemCollapse);
        }

        return getEventHub().subscribe(CollapseEvent.class, (Consumer) listener);
    }

    protected void onItemCollapse(com.vaadin.event.CollapseEvent<E> e) {
        CollapseEvent<E> event = new CollapseEvent<>(WebTreeDataGrid.this,
                e.getCollapsedItem(), e.isUserOriginated());
        publish(CollapseEvent.class, event);
    }
}
