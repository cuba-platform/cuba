/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 25.02.2010 22:56:01
 *
 * $Id$
 */
package com.haulmont.cuba.gui.aggregation.impl;

import java.math.BigDecimal;

public class BigDecimalAggregation extends BasicNumberAggregation<BigDecimal> {

    public BigDecimalAggregation() {
        super(BigDecimal.class);
    }

    public BigDecimal convert(Double result) {
        return BigDecimal.valueOf(result);
    }
}
