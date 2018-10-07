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

package com.haulmont.cuba.gui.components.data.datagrid;

import com.google.common.base.Preconditions;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.data.DataGridItems;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.CollectionDatasource.Sortable.Order;
import com.haulmont.cuba.gui.data.CollectionDatasource.Sortable.SortInfo;

public class SortableDatasourceDataGridItems<E extends Entity<K>, K>
        extends DatasourceDataGridItems<E, K>
        implements DataGridItems.Sortable<E> {

    public SortableDatasourceDataGridItems(CollectionDatasource.Sortable<E, K> datasource) {
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
