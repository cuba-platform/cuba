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
import com.haulmont.cuba.core.entity.Entity;

import java.util.Comparator;

public class EntityComparator<T extends Entity> implements Comparator<T> {
    private MetaProperty property;
    private boolean asc;

    public EntityComparator(MetaProperty property, boolean asc) {
        this.property = property;
        this.asc = asc;

        Class<?> javaClass = property.getJavaField().getType();
        if (!Comparable.class.isAssignableFrom(javaClass))
            throw new UnsupportedOperationException(javaClass + " is not comparable");
    }

    public int compare(T o1, T o2) {
        Comparable<Object> v1 = ((Instance) o1).getValue(property.getName());
        Comparable<Object> v2 = ((Instance) o2).getValue(property.getName());

        return asc ? v1.compareTo(v2) : v2.compareTo(v1);
    }
}
