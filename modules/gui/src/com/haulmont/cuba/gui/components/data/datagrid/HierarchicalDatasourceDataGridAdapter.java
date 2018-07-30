package com.haulmont.cuba.gui.components.data.datagrid;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.data.TreeDataGridSource;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.HierarchicalDatasource;

import java.util.Collection;
import java.util.stream.Stream;

public class HierarchicalDatasourceDataGridAdapter<E extends Entity<K>, K>
        extends SortableCollectionDatasourceDataGridAdapter<E, K>
        implements TreeDataGridSource<E> {

    @SuppressWarnings("unchecked")
    public HierarchicalDatasourceDataGridAdapter(HierarchicalDatasource<E, K> datasource) {
        super((CollectionDatasource.Sortable<E, K>) datasource);
    }

    @SuppressWarnings("unchecked")
    protected HierarchicalDatasource<E, K> getHierarchicalDatasource() {
        return (HierarchicalDatasource<E, K>) datasource;
    }

    @Override
    public int getChildCount(E parent) {
        return Math.toIntExact(getChildren(parent).count());
    }

    @Override
    public Stream<E> getChildren(E item) {
        Collection<K> itemIds = item == null
                ? getHierarchicalDatasource().getRootItemIds()
                : getHierarchicalDatasource().getChildren(item.getId());

        return itemIds.stream()
                .map(id -> datasource.getItem(id));
    }

    @Override
    public boolean hasChildren(E item) {
        return getHierarchicalDatasource().hasChildren(item.getId());
    }
}
