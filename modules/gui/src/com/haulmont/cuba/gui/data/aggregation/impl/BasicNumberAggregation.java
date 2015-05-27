/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.data.aggregation.impl;

import com.haulmont.cuba.gui.data.aggregation.NumberAggregationHelper;

import java.util.Collection;

/**
 * @author gorodnov
 * @version $Id$
 */
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
    public T min(Collection<T> items) {
        NumberAggregationHelper helper = new NumberAggregationHelper();
        for (final T item : items) {
            if (item != null) {
                helper.addItem(item.doubleValue());
            }
        }
        return convert(helper.min());
    }

    protected abstract T convert(Double result);
}