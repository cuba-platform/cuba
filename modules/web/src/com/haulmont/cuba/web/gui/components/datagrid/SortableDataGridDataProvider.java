package com.haulmont.cuba.web.gui.components.datagrid;

import com.haulmont.cuba.gui.components.data.DataGridSource;
import com.haulmont.cuba.web.widgets.data.SortableDataProvider;

public class SortableDataGridDataProvider<T>
        extends DataGridDataProvider<T>
        implements SortableDataProvider<T> {

    public SortableDataGridDataProvider(DataGridSource.Sortable<T> dataGridSource,
                                        DataGridSourceEventsDelegate<T> dataEventsDelegate) {
        super(dataGridSource, dataEventsDelegate);
    }

    public DataGridSource.Sortable<T> getSortableDataGridSource() {
        return (DataGridSource.Sortable<T>) dataGridSource;
    }

    @Override
    public void sort(Object[] propertyId, boolean[] ascending) {
        getSortableDataGridSource().sort(propertyId, ascending);
    }

    @Override
    public void resetSortOrder() {
        getSortableDataGridSource().resetSortOrder();
    }
}
