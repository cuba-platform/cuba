/*
 * Copyright (c) 2008-2018 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.haulmont.cuba.gui.model.impl;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.Sort;
import com.haulmont.cuba.gui.data.impl.EntityComparator;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.model.Sorter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Standard implementation of sorting {@link CollectionContainer}s.
 */
public class CollectionContainerSorter implements Sorter {

    private final CollectionContainer<Entity> container;
    private final CollectionLoader<Entity> loader;

    @SuppressWarnings("unchecked")
    public CollectionContainerSorter(CollectionLoader loader) {
        this.container = loader.getContainer();
        if (this.container == null) {
            throw new IllegalStateException("Container is not set for the loader");
        }
        this.loader = loader;
    }

    @Override
    public void sort(Sort sort) {
        List<? extends Entity> items = container.getItems();
        if (items.isEmpty()) {
            return;
        }
        if (loader.getFirstResult() == 0
                && container.getItems().size() < loader.getMaxResults()) {
            sortInMemory(sort);
        } else {
            reloadWithSort(sort);
        }
    }

    protected void sortInMemory(Sort sort) {
        if (sort.getOrders().isEmpty()) {
            return;
        }
        List<Entity> list = new ArrayList<>(container.getItems());
        list.sort(createComparator(sort, container.getEntityMetaClass()));
        container.setItems(list);
    }

    protected void reloadWithSort(Sort sort) {
        loader.setSort(sort);
        loader.load();
    }

    protected Comparator<Entity> createComparator(Sort sort, MetaClass metaClass) {
        if (sort.getOrders().size() > 1) {
            throw new UnsupportedOperationException("Sort by multiple properties is not supported");
        }
        MetaPropertyPath propertyPath = metaClass.getPropertyPath(sort.getOrders().get(0).getProperty());
        if (propertyPath == null) {
            throw new IllegalArgumentException("Property " + sort.getOrders().get(0).getProperty() + " is invalid");
        }
        boolean asc = sort.getOrders().get(0).getDirection() == Sort.Direction.ASC;
        return new EntityComparator<>(propertyPath, asc);
    }

}
