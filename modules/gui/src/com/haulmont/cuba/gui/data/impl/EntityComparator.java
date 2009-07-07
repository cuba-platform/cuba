/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 30.03.2009 12:23:57
 * $Id$
 */

package com.haulmont.cuba.gui.data.impl;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;

import java.util.Comparator;

public class EntityComparator<T extends Entity> implements Comparator<T> {
    private MetaPropertyPath propertyPath;
    private MetaProperty property;
    private boolean asc;

    public EntityComparator(MetaPropertyPath propertyPath, boolean asc) {
        this.propertyPath = propertyPath;
        if (propertyPath.get().length == 1) {
            property = this.propertyPath.getMetaProperty();
        }
        this.asc = asc;

        Class<?> javaClass = this.propertyPath.getRangeJavaClass();
        if (!Comparable.class.isAssignableFrom(javaClass))
            throw new UnsupportedOperationException(javaClass + " is not comparable");
    }

    public int compare(T o1, T o2) {
        Comparable<Object> v1 = getValue((Instance) o1);
        Comparable<Object> v2 = getValue((Instance) o2);

        int c;
        if (v1 != null && v2 != null) {
            c = v1.compareTo(v2);
        } else if (v1 == null) {
            if (v2 != null) {
                c = 1;
            } else {
                c = 0;
            }
        } else {
            c = -1;
        }

        return asc ? c : -c;
    }

    private Comparable<Object> getValue(Instance instance) {
        if (property != null) {
            return instance.getValue(property.getName());
        }
        return instance.getValueEx(propertyPath.toString());
    }
}
