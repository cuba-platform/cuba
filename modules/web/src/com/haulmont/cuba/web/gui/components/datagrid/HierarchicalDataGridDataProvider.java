package com.haulmont.cuba.web.gui.components.datagrid;

import com.haulmont.cuba.gui.components.data.BindingState;
import com.haulmont.cuba.gui.components.data.TreeDataGridSource;
import com.vaadin.data.provider.HierarchicalDataProvider;
import com.vaadin.data.provider.HierarchicalQuery;
import com.vaadin.server.SerializablePredicate;

import java.util.stream.Stream;

public class HierarchicalDataGridDataProvider<T> extends SortableDataGridDataProvider<T>
        implements HierarchicalDataProvider<T, SerializablePredicate<T>> {


    public HierarchicalDataGridDataProvider(TreeDataGridSource<T> dataGridSource,
                                            DataGridSourceEventsDelegate<T> dataEventsDelegate) {
        super(dataGridSource, dataEventsDelegate);
    }

    public TreeDataGridSource<T> getTreeDataGridSource() {
        return (TreeDataGridSource<T>) dataGridSource;
    }

    @Override
    public int getChildCount(HierarchicalQuery<T, SerializablePredicate<T>> query) {
        if (getTreeDataGridSource().getState() == BindingState.INACTIVE) {
            return 0;
        }

        return getTreeDataGridSource().getChildCount(query.getParent());
    }

    @Override
    public Stream<T> fetchChildren(HierarchicalQuery<T, SerializablePredicate<T>> query) {
        if (dataGridSource.getState() == BindingState.INACTIVE) {
            return Stream.empty();
        }

        return getTreeDataGridSource().getChildren(query.getParent())
                .skip(query.getOffset())
                .limit(query.getLimit());
    }

    @Override
    public boolean hasChildren(T item) {
        return getTreeDataGridSource().hasChildren(item);
    }
}
