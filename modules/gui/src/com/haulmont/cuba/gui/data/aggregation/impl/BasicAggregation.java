/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.data.aggregation.impl;

import com.haulmont.cuba.gui.data.aggregation.Aggregation;

import java.util.Collection;

/**
 * @author gorodnov
 * @version $Id$
 */
public class BasicAggregation<T> implements Aggregation<T> {

    private Class<T> clazz;

    public BasicAggregation(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T sum(Collection<T> items) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T avg(Collection<T> items) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T min(Collection<T> items) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T max(Collection<T> items) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int count(Collection<T> items) {
        return items.size();
    }

    @Override
    public Class<T> getResultClass() {
        return clazz;
    }
}