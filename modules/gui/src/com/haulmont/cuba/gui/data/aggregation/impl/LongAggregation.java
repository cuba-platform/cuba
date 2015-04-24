/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.data.aggregation.impl;

/**
 * @author gorodnov
 * @version $Id$
 */
public class LongAggregation extends BasicNumberAggregation<Long> {
    public LongAggregation() {
        super(Long.class);
    }

    @Override
    protected Long convert(Double result) {
        return result.longValue();
    }
}