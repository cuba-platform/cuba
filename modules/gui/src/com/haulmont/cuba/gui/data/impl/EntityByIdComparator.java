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
 *
 */
package com.haulmont.cuba.gui.data.impl;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.data.CollectionDatasource;

public class EntityByIdComparator<T extends Entity<K>, K> extends AbstractComparator<K> {
    private MetaPropertyPath propertyPath;
    private MetaProperty property;
    private CollectionDatasource datasource;

    public EntityByIdComparator(MetaPropertyPath propertyPath, CollectionDatasource<T, K> datasource, boolean asc) {
        super(asc);
        this.propertyPath = propertyPath;
        if (propertyPath.getMetaProperties().length == 1) {
            property = this.propertyPath.getMetaProperty();
        }
        this.datasource = datasource;
    }

    @Override
    public int compare(K key1, K key2) {
        Object o1 = getValue(datasource.getItem(key1));
        Object o2 = getValue(datasource.getItem(key2));

        return __compare(o1, o2);
    }

    private Object getValue(Instance instance) {
        Object value;
        if (property != null) {
            value = instance.getValue(property.getName());
        } else {
            value = instance.getValueEx(propertyPath.toString());
        }

        if (!(value == null || value instanceof Comparable)) {
            value = value.toString();
        }

        return value;
    }
}