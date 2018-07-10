package com.haulmont.cuba.gui.components.data.datagrid;

import com.google.common.base.Preconditions;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.data.DataGridSource;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.CollectionDatasource.Sortable.Order;
import com.haulmont.cuba.gui.data.CollectionDatasource.Sortable.SortInfo;

public class SortableCollectionDatasourceDataGridAdapter<E extends Entity<K>, K>
        extends CollectionDatasourceDataGridAdapter<E, K>
        implements DataGridSource.Sortable<E> {

    public SortableCollectionDatasourceDataGridAdapter(CollectionDatasource.Sortable<E, K> datasource) {
        super(datasource);
    }

    @SuppressWarnings("unchecked")
    protected CollectionDatasource.Sortable<E, K> getSortableDatasource() {
        return (CollectionDatasource.Sortable<E, K>) datasource;
    }

    @Override
    public void sort(Object[] propertyIds, boolean[] ascendingFlags) {
        // A datasource supports sort only by single property
        Preconditions.checkArgument(propertyIds.length == 1);
        Preconditions.checkArgument(ascendingFlags.length == 1);

        MetaPropertyPath propertyPath = (MetaPropertyPath) propertyIds[0];
        boolean ascending = ascendingFlags[0];

        SortInfo<MetaPropertyPath> info = new SortInfo<>();
        info.setPropertyPath(propertyPath);
        info.setOrder(ascending ? Order.ASC : Order.DESC);

        getSortableDatasource().sort(new SortInfo[]{info});
    }

    @Override
    public void resetSortOrder() {
        getSortableDatasource().resetSortOrder();
    }
}
