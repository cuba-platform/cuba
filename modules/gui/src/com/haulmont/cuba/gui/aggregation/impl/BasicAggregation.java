/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.aggregation.impl;

import com.haulmont.cuba.gui.aggregation.Aggregation;

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
    public boolean allowSum() {
        return false;
    }

    @Override
    public T avg(Collection<T> items) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean allowAvg() {
        return false;
    }

    @Override
    public T min(Collection<T> items) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean allowMin() {
        return false;
    }

    @Override
    public T max(Collection<T> items) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean allowMax() {
        return false;
    }

    @Override
    public int count(Collection<T> items) {
        return items.size();
    }

    @Override
    public boolean allowCount() {
        return true;
    }

    @Override
    public Class<T> getJavaClass() {
        return clazz;
    }
}