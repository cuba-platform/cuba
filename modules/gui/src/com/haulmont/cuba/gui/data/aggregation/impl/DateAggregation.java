/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.data.aggregation.impl;

import com.haulmont.cuba.gui.data.aggregation.NumberAggregationHelper;

import java.util.Date;
import java.util.Collection;

/**
 * @author gorodnov
 * @version $Id$
 */
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
        Double result = helper.min();
        return result != null ? new Date(result.longValue()) : null;
    }

    @Override
    public Date max(Collection<Date> items) {
        NumberAggregationHelper helper = new NumberAggregationHelper();
        for (final Date item : items) {
            if (item != null) {
                helper.addItem((double) item.getTime());
            }
        }
        Double result = helper.max();
        return result != null ? new Date(result.longValue()) : null;
    }
}