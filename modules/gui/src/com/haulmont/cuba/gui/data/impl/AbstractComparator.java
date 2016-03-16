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
import com.haulmont.cuba.client.sys.PersistenceManagerClient;
import com.haulmont.cuba.core.global.AppBeans;

import java.util.Comparator;

/**
 */
public abstract class AbstractComparator<T> implements Comparator<T> {

    protected boolean asc;

    protected int nullsLast =
            AppBeans.get(PersistenceManagerClient.NAME, PersistenceManagerClient.class).isNullsLastSorting() ?
                    1 : -1;

    protected AbstractComparator(boolean asc) {
        this.asc = asc;
    }

    protected int __compare(Object o1, Object o2) {
        int c;

        if (o1 instanceof String && o2 instanceof String) {
            c = ((String) o1).compareToIgnoreCase((String) o2);
        } else if (o1 instanceof Comparable && o2 instanceof Comparable) {
            c = ((Comparable) o1).compareTo(o2);
        } else if (o1 instanceof Instance && o2 instanceof Instance) {
            c = ((Instance) o1).getInstanceName().compareToIgnoreCase(((Instance) o2).getInstanceName());
        } else if (o1 == null) {
            if (o2 != null) {
                c = nullsLast;
            } else {
                c = 0;
            }
        } else {
            c = -nullsLast;
        }

        return asc ? c : -c;
    }
}