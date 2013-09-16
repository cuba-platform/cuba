/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.aggregation.impl;

public class LongAggregation extends BasicNumberAggregation<Long> {
    public LongAggregation() {
        super(Long.class);
    }

    protected Long convert(Double result) {
        return result.longValue();
    }
}
