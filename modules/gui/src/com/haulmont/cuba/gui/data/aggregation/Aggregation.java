/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.data.aggregation;

import java.util.Collection;

/**
 * @author gorodnov
 * @version $Id$
 *
 * @param <T>
 */
public interface Aggregation<T> {

    T sum(Collection<T> items);

    T avg(Collection<T> items);

    T min(Collection<T> items);

    T max(Collection<T> items);

    int count(Collection<T> items);

    Class<T> getResultClass();
}