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

package com.haulmont.cuba.gui.model.impl;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesUtils;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.Sort;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.Sorter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Base implementation of sorting collection containers.
 */
public abstract class BaseContainerSorter implements Sorter {

    private final CollectionContainer container;

    public BaseContainerSorter(CollectionContainer container) {
        this.container = container;
    }

    public CollectionContainer getContainer() {
        return container;
    }

    @Override
    public void sort(Sort sort) {
        sortInMemory(sort);
    }

    @SuppressWarnings("unchecked")
    protected void sortInMemory(Sort sort) {
        if (sort.getOrders().isEmpty() || container.getItems().isEmpty()) {
            return;
        }
        List list = new ArrayList(container.getItems());
        list.sort(createComparator(sort, container.getEntityMetaClass()));
        setItemsToContainer(list);
    }

    protected abstract void setItemsToContainer(List list);

    protected Comparator<? extends Entity> createComparator(Sort sort, MetaClass metaClass) {
        if (sort.getOrders().size() > 1) {
            throw new UnsupportedOperationException("Sort by multiple properties is not supported");
        }

        String propertyName = sort.getOrders().get(0).getProperty();
        boolean asc = sort.getOrders().get(0).getDirection() == Sort.Direction.ASC;

        if (DynamicAttributesUtils.isDynamicAttribute(propertyName)) {
            return Comparator.comparing(e -> e.getValueEx(propertyName), EntityValuesComparator.asc(asc));
        }

        MetaPropertyPath propertyPath = metaClass.getPropertyPath(propertyName);
        if (propertyPath == null) {
            throw new IllegalArgumentException("Property " + propertyName + " is invalid");
        }
        return Comparator.comparing(e -> e.getValueEx(propertyPath), EntityValuesComparator.asc(asc));
    }
}
