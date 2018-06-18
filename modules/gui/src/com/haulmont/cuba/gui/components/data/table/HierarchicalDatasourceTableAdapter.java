package com.haulmont.cuba.gui.components.data.table;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.data.TreeTableSource;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.HierarchicalDatasource;

import java.util.Collection;

public class HierarchicalDatasourceTableAdapter<E extends Entity<K>, K>
        extends SortableCollectionDatasourceTableAdapter<E, K>
        implements TreeTableSource<E> {

    @SuppressWarnings("unchecked")
    public HierarchicalDatasource<E, K> getTreeDatasource() {
        return (HierarchicalDatasource<E, K>) datasource;
    }

    @SuppressWarnings("unchecked")
    public HierarchicalDatasourceTableAdapter(HierarchicalDatasource<E, K> datasource) {
        super((CollectionDatasource.Sortable<E, K>) datasource);
    }

    @Override
    public String getHierarchyPropertyName() {
        return getTreeDatasource().getHierarchyPropertyName();
    }

    @Override
    public Collection getRootItemIds() {
        return getTreeDatasource().getRootItemIds();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object getParent(Object itemId) {
        return getTreeDatasource().getParent((K) itemId);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<?> getChildren(Object itemId) {
        return getTreeDatasource().getChildren((K) itemId);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean isRoot(Object itemId) {
        return getTreeDatasource().isRoot((K) itemId);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean hasChildren(Object itemId) {
        return getTreeDatasource().hasChildren((K) itemId);
    }
}