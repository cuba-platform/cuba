package com.haulmont.cuba.web.gui.components;

import com.google.common.base.Strings;
import com.haulmont.bali.events.Subscription;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.TreeDataGrid;
import com.haulmont.cuba.gui.components.data.DataGridItems;
import com.haulmont.cuba.gui.components.data.TreeDataGridItems;
import com.haulmont.cuba.web.gui.components.datagrid.DataGridDataProvider;
import com.haulmont.cuba.web.gui.components.datagrid.HierarchicalDataGridDataProvider;
import com.haulmont.cuba.web.widgets.CubaTreeGrid;
import com.vaadin.data.TreeData;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.TreeDataProvider;
import com.vaadin.shared.Registration;
import com.vaadin.ui.Grid;
import org.dom4j.Element;

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

    protected Column<E> hierarchyColumn;

    @Override
    protected CubaTreeGrid<E> createComponent() {
        return new CubaTreeGrid<>();
    }

    @Override
    protected void onColumnReorder(Grid.ColumnReorderEvent e) {
        super.onColumnReorder(e);

        String[] columnOrder = getColumnOrder();
        // if the hierarchy column isn't set explicitly,
        // we set the first column as the hierarchy column
        if (getHierarchyColumn() == null
                && columnOrder.length > 0) {
            String columnId = columnOrder[0];
            component.setHierarchyColumn(columnId);
        }
    }

    @Nullable
    @Override
    public TreeDataGridItems<E> getItems() {
        return (TreeDataGridItems<E>) super.getItems();
    }

    @Override
    public void setItems(DataGridItems<E> dataGridItems) {
        if (dataGridItems != null
                && !(dataGridItems instanceof TreeDataGridItems)) {
            throw new IllegalArgumentException("TreeDataGrid supports only TreeDataGridItems data binding");
        }

        super.setItems(dataGridItems);
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
    protected DataGridDataProvider<E> createDataGridDataProvider(DataGridItems<E> dataGridItems) {
        return new HierarchicalDataGridDataProvider<>((TreeDataGridItems<E>) dataGridItems, this);
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
        return hierarchyColumn;
    }

    @Override
    public void setHierarchyColumn(String id) {
        setHierarchyColumn(getColumnNN(id));
    }

    @Override
    public void setHierarchyColumn(Column<E> column) {
        checkNotNullArgument(column);

        hierarchyColumn = column;

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
        if (getItems() != null) {
            expandRecursively(getItems().getChildren(null), Integer.MAX_VALUE);
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
        if (getItems() != null) {
            collapseRecursively(getItems().getChildren(null), Integer.MAX_VALUE);
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

    @Override
    public void applySettings(Element element) {
        super.applySettings(element);

        if (!isSettingsEnabled()) {
            return;
        }

        String hierarchyColumn = element.attributeValue("hierarchyColumn");
        if (!Strings.isNullOrEmpty(hierarchyColumn)
                && getColumn(hierarchyColumn) != null) {
            setHierarchyColumn(hierarchyColumn);
        }
    }

    @Override
    public boolean saveSettings(Element element) {
        boolean settingsChanged = super.saveSettings(element);

        if (!isSettingsEnabled()) {
            return false;
        }

        Column<E> hierarchyColumn = getHierarchyColumn();
        if (hierarchyColumn != null
                && !hierarchyColumn.getId().equals(element.attributeValue("hierarchyColumn"))) {
            element.addAttribute("hierarchyColumn", hierarchyColumn.getId());
            settingsChanged = true;
        }

        return settingsChanged;
    }
}
