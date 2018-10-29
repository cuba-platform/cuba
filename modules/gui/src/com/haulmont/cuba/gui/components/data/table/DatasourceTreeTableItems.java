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

package com.haulmont.cuba.gui.components.data.table;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.data.TreeTableItems;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.HierarchicalDatasource;

import java.util.Collection;

public class DatasourceTreeTableItems<E extends Entity<K>, K>
        extends SortableDatasourceTableItems<E, K>
        implements TreeTableItems<E> {

    @SuppressWarnings("unchecked")
    public HierarchicalDatasource<E, K> getTreeDatasource() {
        return (HierarchicalDatasource<E, K>) datasource;
    }

    @SuppressWarnings("unchecked")
    public DatasourceTreeTableItems(HierarchicalDatasource<E, K> datasource) {
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

    @Override
    public Object firstItemId() {
        Collection<?> rootItemIds = getRootItemIds();
        return rootItemIds.isEmpty() ? null : rootItemIds.iterator().next();
    }
}