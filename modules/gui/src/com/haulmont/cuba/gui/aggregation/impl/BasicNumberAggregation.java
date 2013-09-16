/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.aggregation.impl;

import com.haulmont.cuba.gui.aggregation.NumberAggregationHelper;

import java.util.Collection;

public abstract class BasicNumberAggregation<T extends Number> extends BasicAggregation <T> {

    protected BasicNumberAggregation(Class<T> clazz) {
        super(clazz);
    }

    @Override
    public T sum(Collection<T> items) {
        NumberAggregationHelper helper = new NumberAggregationHelper();
        for (final T item : items) {
            if (item != null) {
                helper.addItem(item.doubleValue());
            }
        }
        return convert(helper.sum());
    }

    @Override
    public boolean allowSum() {
        return true;
    }

    @Override
    public T avg(Collection<T> items) {
        NumberAggregationHelper helper = new NumberAggregationHelper();
        for (final T item : items) {
            if (item != null) {
                helper.addItem(item.doubleValue());
            }
        }
        return convert(helper.avg());
    }

    @Override
    public boolean allowAvg() {
        return true;
    }

    @Override
    public T max(Collection<T> items) {
        NumberAggregationHelper helper = new NumberAggregationHelper();
        for (final T item : items) {
            if (item != null) {
                helper.addItem(item.doubleValue());
            }
        }
        return convert(helper.max());
    }

    @Override
    public boolean allowMax() {
        return true;
    }

    @Override
    public T min(Collection<T> items) {
        NumberAggregationHelper helper = new NumberAggregationHelper();
        for (final T item : items) {
            if (item != null) {
                helper.addItem(item.doubleValue());
            }
        }
        return convert(helper.min());
    }

    @Override
    public boolean allowMin() {
        return true;
    }

    protected abstract T convert(Double result);
}
