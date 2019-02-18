/*
 * Copyright (c) 2008-2019 Haulmont.
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

import com.haulmont.chile.core.model.Instance;
import com.haulmont.cuba.core.entity.IdProxy;
import com.haulmont.cuba.gui.data.impl.AbstractComparator;

import java.util.Comparator;

/**
 * A comparison function, which imposes a ordering for entity attribute values.
 * <p>
 * For example, to obtain a {@code Comparator} that compares {@code com.haulmont.cuba.core.entity.Entity} objects
 * by some property that is specified by {@code com.haulmont.chile.core.model.MetaPropertyPath}:
 * <pre>{@code Comparator.comparing(e -> e.getValueEx(propertyPath), EntityValuesComparator.of(asc))}</pre>
 */
public class EntityValuesComparator<T> extends AbstractComparator<T> {
    public static final Comparator<Object> NATURAL_ORDER = new EntityValuesComparator<>(true);
    public static final Comparator<Object> REVERSE_ORDER = new EntityValuesComparator<>(false);

    public static Comparator<Object> asc(boolean asc) {
        return asc ? NATURAL_ORDER : REVERSE_ORDER;
    }

    protected EntityValuesComparator(boolean asc) {
        super(asc);
    }

    @Override
    public int compare(T o1, T o2) {
        return __compare(transformValue(o1), transformValue(o2));
    }

    protected Object transformValue(T value) {
        Object newValue = value;
        if (!(value == null || value instanceof Comparable || value instanceof Instance)) {
            if (value instanceof IdProxy) {
                newValue = ((IdProxy) value).get();
            } else {
                newValue = value.toString();
            }
        }
        return newValue;
    }
}
