/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.aggregation;

import com.haulmont.chile.core.datatypes.Datatype;

import java.util.Collection;

public interface Aggregation<T> {

    T sum(Collection<T> items);
    boolean allowSum();

    T avg(Collection<T> items);
    boolean allowAvg();

    T min(Collection<T> items);
    boolean allowMin();

    T max(Collection<T> items);
    boolean allowMax();

    int count(Collection<T> items);
    boolean allowCount();

    Class<T> getJavaClass();
}
