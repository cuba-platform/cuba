package com.haulmont.cuba.gui.components;

import com.haulmont.bali.events.Subscription;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.data.DataGridSource;
import com.haulmont.cuba.gui.components.data.datagrid.HierarchicalDatasourceDataGridAdapter;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.HierarchicalDatasource;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.EventObject;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

// TODO: gg, JavaDoc
public interface TreeDataGrid<E extends Entity> extends DataGrid<E> {

    String NAME = "treeDataGrid";

    @Override
    default HierarchicalDatasource getDatasource() {
        DataGridSource<E> dataGridSource = getDataGridSource();
        return dataGridSource != null ?
                (HierarchicalDatasource) ((HierarchicalDatasourceDataGridAdapter) dataGridSource).getDatasource()
                : null;
    }

    @Deprecated
    default void setDatasource(HierarchicalDatasource datasource) {
        setDatasource(datasource);
    }

    @SuppressWarnings("unchecked")
    @Override
    default void setDatasource(CollectionDatasource datasource) {
        if (datasource == null) {
            setDataGridSource(null);
        } else {
            if (!(datasource instanceof HierarchicalDatasource)) {
                throw new IllegalArgumentException("TreeDataGrid supports only HierarchicalDatasource");
            }

            setDataGridSource(new HierarchicalDatasourceDataGridAdapter((HierarchicalDatasource) datasource));
        }
    }

    @Nullable
    Column<E> getHierarchyColumn();

    void setHierarchyColumn(String id);

    void setHierarchyColumn(Column<E> column);

    Predicate<E> getItemCollapseAllowedProvider();

    void setItemCollapseAllowedProvider(Predicate<E> provider);

    default void expand(E... items) {
        expand(Arrays.asList(items));
    }

    void expand(Collection<E> items);

    default void expandRecursively(Collection<E> items, int depth) {
        expandRecursively(items.stream(), depth);
    }

    void expandRecursively(Stream<E> items, int depth);

    default void collapse(E... items) {
        collapse(Arrays.asList(items));
    }

    void collapse(Collection<E> items);

    default void collapseRecursively(Collection<E> items, int depth) {
        collapseRecursively(items.stream(), depth);
    }

    void collapseRecursively(Stream<E> items, int depth);

    boolean isExpanded(E item);

    Subscription addExpandListener(Consumer<ExpandEvent<E>> listener);

    Subscription addCollapseListener(Consumer<CollapseEvent<E>> listener);

    class ExpandEvent<E extends Entity> extends EventObject implements HasUserOriginated {

        protected final E expandedItem;
        protected final boolean userOriginated;

        public ExpandEvent(TreeDataGrid<E> source, E expandedItem, boolean userOriginated) {
            super(source);
            this.expandedItem = expandedItem;
            this.userOriginated = userOriginated;
        }

        @SuppressWarnings("unchecked")
        @Override
        public TreeDataGrid<E> getSource() {
            return (TreeDataGrid<E>) super.getSource();
        }

        public E getExpandedItem() {
            return expandedItem;
        }

        @Override
        public boolean isUserOriginated() {
            return userOriginated;
        }
    }

    class CollapseEvent<E extends Entity> extends EventObject implements HasUserOriginated {

        protected final E collapsedItem;
        protected final boolean userOriginated;

        public CollapseEvent(TreeDataGrid<E> source, E collapsedItem, boolean userOriginated) {
            super(source);
            this.collapsedItem = collapsedItem;
            this.userOriginated = userOriginated;
        }

        @SuppressWarnings("unchecked")
        @Override
        public TreeDataGrid<E> getSource() {
            return (TreeDataGrid<E>) super.getSource();
        }

        public E getCollapsedItem() {
            return collapsedItem;
        }

        @Override
        public boolean isUserOriginated() {
            return userOriginated;
        }
    }
}
