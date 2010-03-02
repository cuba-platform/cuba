/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 25.02.2010 22:30:35
 *
 * $Id$
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
