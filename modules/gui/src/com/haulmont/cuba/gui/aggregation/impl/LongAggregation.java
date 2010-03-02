/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 27.02.2010 1:00:41
 *
 * $Id$
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
