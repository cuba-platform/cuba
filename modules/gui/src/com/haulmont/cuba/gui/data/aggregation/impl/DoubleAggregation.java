/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.data.aggregation.impl;

/**
 * @author gorodnov
 * @version $Id$
 */
public class DoubleAggregation extends BasicNumberAggregation<Double> {
    public DoubleAggregation() {
        super(Double.class);
    }

    @Override
    public Double convert(Double result) {
        return result;
    }
}