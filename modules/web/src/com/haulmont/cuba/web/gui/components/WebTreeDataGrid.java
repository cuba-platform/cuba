package com.haulmont.cuba.web.gui.components;

import com.haulmont.bali.events.Subscription;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.TreeDataGrid;
import com.haulmont.cuba.gui.components.data.DataGridSource;
import com.haulmont.cuba.gui.components.data.TreeDataGridSource;
import com.haulmont.cuba.web.gui.components.datagrid.DataGridDataProvider;
import com.haulmont.cuba.web.gui.components.datagrid.HierarchicalDataGridDataProvider;
import com.haulmont.cuba.web.widgets.CubaTreeGrid;
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

    @Override
    protected CubaTreeGrid<E> createComponent() {
        return new CubaTreeGrid<>();
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
    protected void initComponent(Grid<E> component) {
        super.initComponent(component);

        CubaTreeGrid<E> treeGrid = (CubaTreeGrid<E>) component;
        treeGrid.setItemCollapseAllowedProvider(itemCollapseAllowedProvider::test);
        treeGrid.addExpandListener(createExpandListener());
        treeGrid.addCollapseListener(createCollapseListener());
    }

    protected com.vaadin.event.ExpandEvent.ExpandListener<E> createExpandListener() {
        return e -> {
            ExpandEvent<E> event = new ExpandEvent<>(WebTreeDataGrid.this,
                    e.getExpandedItem(), e.isUserOriginated());
            publish(ExpandEvent.class, event);
        };
    }

    protected com.vaadin.event.CollapseEvent.CollapseListener<E> createCollapseListener() {
        return e -> {
            CollapseEvent<E> event = new CollapseEvent<>(WebTreeDataGrid.this,
                    e.getCollapsedItem(), e.isUserOriginated());
            publish(CollapseEvent.class, event);
        };
    }

    protected TreeDataGridSource<E> getTreeDataGridSource() {
        return (TreeDataGridSource<E>) getDataGridSource();
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
    public void collapse(Collection<E> items) {
        component.collapse(items);
    }

    @Override
    public void collapseRecursively(Stream<E> items, int depth) {
        component.collapseRecursively(items, depth);
    }

    @Override
    public boolean isExpanded(E item) {
        return component.isExpanded(item);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Subscription addExpandListener(Consumer<ExpandEvent<E>> listener) {
        return subscribe(ExpandEvent.class, (Consumer) listener);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Subscription addCollapseListener(Consumer<CollapseEvent<E>> listener) {
        return subscribe(CollapseEvent.class, (Consumer) listener);
    }
}
