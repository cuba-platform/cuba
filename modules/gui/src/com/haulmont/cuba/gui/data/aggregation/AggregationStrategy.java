/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.data.aggregation;

import java.util.Collection;

/**
 * @author artamonov
 * @version $Id$
 *
 * @param <T> type of aggregation result
 * @param <V> type of property values
 */
public interface AggregationStrategy<V, T> {
    T aggregate(Collection<V> propertyValues);

    Class<T> getResultClass();
}