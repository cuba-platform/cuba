/*
 * Copyright (c) 2008-2018 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.haulmont.cuba.web.gui.components.datagrid;

import com.haulmont.cuba.gui.components.data.DataGridItems;
import com.haulmont.cuba.web.widgets.data.SortableDataProvider;

public class SortableDataGridDataProvider<T>
        extends DataGridDataProvider<T>
        implements SortableDataProvider<T> {

    public SortableDataGridDataProvider(DataGridItems.Sortable<T> dataGridSource,
                                        DataGridItemsEventsDelegate<T> dataEventsDelegate) {
        super(dataGridSource, dataEventsDelegate);
    }

    public DataGridItems.Sortable<T> getSortableDataGridSource() {
        return (DataGridItems.Sortable<T>) dataGridItems;
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
