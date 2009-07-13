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
        Object v1 = getValue((Instance) o1);
        Object v2 = getValue((Instance) o2);
        int c;

        if ((v1 instanceof String) && (v2 instanceof String)) {
            c = ((String) v1).compareToIgnoreCase((String) v2);
        } else {
            Comparable c1 = (Comparable) v1;
            Comparable c2 = (Comparable) v2;

            if (c1 != null && c2 != null) {
                c = c1.compareTo(c2);
            } else if (c1 == null) {
                if (c2 != null) {
                    c = 1;
                } else {
                    c = 0;
                }
            } else {
                c = -1;
            }
        }
        return asc ? c : -c;
    }

    private Object getValue(Instance instance) {
        if (property != null) {
            return instance.getValue(property.getName());
        }
        return instance.getValueEx(propertyPath.toString());
    }
}
