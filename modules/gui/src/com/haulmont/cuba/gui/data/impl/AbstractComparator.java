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
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.client.sys.PersistenceManagerClient;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;

import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;

public abstract class AbstractComparator<T> implements Comparator<T> {

    protected boolean asc;

    protected int nullsLast;

    protected Metadata metadata;

    protected AbstractComparator(boolean asc) {
        PersistenceManagerClient persistenceManager = AppBeans.get(PersistenceManagerClient.NAME, PersistenceManagerClient.class);
        this.metadata = AppBeans.get(Metadata.NAME);
        this.asc = asc;
        this.nullsLast = persistenceManager.isNullsLastSorting() ? 1 : -1;
    }

    protected int __compare(Object o1, Object o2) {
        int c = compareAsc(o1, o2);
        return asc ? c : -c;
    }

    protected int compareAsc(Object o1, Object o2) {
        int c;
        if (o1 instanceof String && o2 instanceof String) {
            c = ((String) o1).compareToIgnoreCase((String) o2);
        } else if (o1 instanceof Comparable && o2 instanceof Comparable) {
            c = ((Comparable) o1).compareTo(o2);
        } else if (o1 instanceof Instance && o2 instanceof Instance) {
            MetaClass metaClass = metadata.getClassNN(o1.getClass());
            Collection<MetaProperty> namePatternProperties = metadata.getTools().getNamePatternProperties(metaClass, true);
            if (namePatternProperties.isEmpty()) {
                c = ((Instance) o1).getInstanceName().compareToIgnoreCase(((Instance) o2).getInstanceName());
            } else {
                c = 0;
                for (MetaProperty property : namePatternProperties) {
                    Object v1 = ((Instance) o1).getValue(property.getName());
                    Object v2 = ((Instance) o2).getValue(property.getName());
                    c = compareAsc(v1, v2);
                    if (c != 0)
                        break;
                }
            }
        } else if (Objects.equals(o1, o2)) {
            c = 0;
        } else if (o1 == null && o2 != null) {
            c = nullsLast;
        } else {
            c = -nullsLast;
        }
        return c;
    }
}