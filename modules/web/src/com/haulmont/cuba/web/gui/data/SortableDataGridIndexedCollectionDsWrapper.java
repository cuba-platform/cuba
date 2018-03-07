/*
 * Copyright (c) 2008-2016 Haulmont.
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

package com.haulmont.cuba.web.gui.data;

import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.web.gui.components.WebDataGrid.CollectionDsListenersWrapper;
import com.vaadin.v7.data.Container;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static com.haulmont.cuba.gui.data.CollectionDatasource.Sortable.Order;
import static com.haulmont.cuba.gui.data.CollectionDatasource.Sortable.SortInfo;

public class SortableDataGridIndexedCollectionDsWrapper
        extends
            DataGridIndexedCollectionDsWrapper
        implements
            Container.Sortable {

    public SortableDataGridIndexedCollectionDsWrapper(CollectionDatasource datasource, boolean autoRefresh,
                                                      CollectionDsListenersWrapper collectionDsListenersWrapper) {
        super(datasource, autoRefresh, collectionDsListenersWrapper);
    }

    @SuppressWarnings("unchecked")
    public SortableDataGridIndexedCollectionDsWrapper(CollectionDatasource datasource,
                                                      Collection<MetaPropertyPath> properties,
                                                      boolean autoRefresh,
                                                      CollectionDsListenersWrapper collectionDsListenersWrapper) {
        super(datasource, properties, autoRefresh, collectionDsListenersWrapper);
    }

    @Override
    public void sort(Object[] propertyId, boolean[] ascending) {
        List<SortInfo> infos = new LinkedList<>();
        for (int i = 0; i < propertyId.length; i++) {
            final MetaPropertyPath propertyPath = (MetaPropertyPath) propertyId[i];

            final SortInfo<MetaPropertyPath> info = new SortInfo<>();
            info.setPropertyPath(propertyPath);
            info.setOrder(ascending[i] ? Order.ASC : Order.DESC);

            infos.add(info);
        }
        SortInfo[] sortInfos = infos.toArray(new SortInfo[infos.size()]);
        ((CollectionDatasource.Sortable) datasource).sort(sortInfos);
    }

    @Override
    public Collection<?> getSortableContainerPropertyIds() {
        return properties;
    }
}
