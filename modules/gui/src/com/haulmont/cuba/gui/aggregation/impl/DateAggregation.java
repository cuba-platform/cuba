/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.aggregation.impl;

import com.haulmont.cuba.gui.aggregation.NumberAggregationHelper;

import java.util.Date;
import java.util.Collection;

public class DateAggregation extends BasicAggregation<Date> {
    public DateAggregation() {
        super(Date.class);
    }

    @Override
    public Date min(Collection<Date> items) {
        NumberAggregationHelper helper = new NumberAggregationHelper();
        for (final Date item : items) {
            if (item != null) {
                helper.addItem((double) item.getTime());
            }
        }
        return new Date(helper.min().longValue());
    }

    @Override
    public boolean allowMin() {
        return true;
    }

    @Override
    public Date max(Collection<Date> items) {
        NumberAggregationHelper helper = new NumberAggregationHelper();
        for (final Date item : items) {
            if (item != null) {
                helper.addItem((double) item.getTime());
            }
        }
        return new Date(helper.max().longValue());
    }

    @Override
    public boolean allowMax() {
        return true;
    }
}
