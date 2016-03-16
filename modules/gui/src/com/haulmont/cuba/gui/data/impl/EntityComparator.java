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

/**
 */
public class EntityComparator<T extends Entity> extends AbstractComparator<T> {
    private MetaPropertyPath propertyPath;
    private MetaProperty property;

    public EntityComparator(MetaPropertyPath propertyPath, boolean asc) {
        super(asc);
        this.propertyPath = propertyPath;
        if (propertyPath.getMetaProperties().length == 1) {
            property = this.propertyPath.getMetaProperty();
        }

/*
        Class<?> javaClass = this.propertyPath.getRangeJavaClass();
        if (!Comparable.class.isAssignableFrom(javaClass))
            throw new UnsupportedOperationException(javaClass + " is not comparable");
*/
    }

    @Override
    public int compare(T o1, T o2) {
        Object v1 = getValue(o1);
        Object v2 = getValue(o2);
        
        return __compare(v1, v2);
    }

    private Object getValue(Instance instance) {
        Object value;
        if (property != null) {
            value = instance.getValue(property.getName());
        } else {
            value = instance.getValueEx(propertyPath.toString());
        }

        if (!(value == null || value instanceof Comparable || value instanceof Instance)) {
            value = value.toString();
        }

        return value;
    }
}