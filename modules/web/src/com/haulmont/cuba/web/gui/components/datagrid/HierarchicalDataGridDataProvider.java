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

import com.haulmont.cuba.gui.components.data.BindingState;
import com.haulmont.cuba.gui.components.data.TreeDataGridItems;
import com.vaadin.data.provider.HierarchicalDataProvider;
import com.vaadin.data.provider.HierarchicalQuery;
import com.vaadin.server.SerializablePredicate;

import java.util.stream.Stream;

public class HierarchicalDataGridDataProvider<T> extends SortableDataGridDataProvider<T>
        implements HierarchicalDataProvider<T, SerializablePredicate<T>> {


    public HierarchicalDataGridDataProvider(TreeDataGridItems<T> dataGridSource,
                                            DataGridItemsEventsDelegate<T> dataEventsDelegate) {
        super(dataGridSource, dataEventsDelegate);
    }

    public TreeDataGridItems<T> getTreeDataGridSource() {
        return (TreeDataGridItems<T>) dataGridItems;
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
        if (dataGridItems.getState() == BindingState.INACTIVE) {
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
