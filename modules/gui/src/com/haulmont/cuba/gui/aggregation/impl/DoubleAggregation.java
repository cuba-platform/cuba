/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 27.02.2010 1:01:50
 *
 * $Id$
 */
package com.haulmont.cuba.gui.aggregation.impl;

public class DoubleAggregation extends BasicNumberAggregation<Double> {
    public DoubleAggregation() {
        super(Double.class);
    }

    public Double convert(Double result) {
        return result;
    }
}
