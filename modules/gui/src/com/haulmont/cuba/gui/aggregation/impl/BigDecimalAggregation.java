/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.aggregation.impl;

import java.math.BigDecimal;

/**
 * @author gorodnov
 * @version $Id$
 */
public class BigDecimalAggregation extends BasicNumberAggregation<BigDecimal> {

    public BigDecimalAggregation() {
        super(BigDecimal.class);
    }

    @Override
    public BigDecimal convert(Double result) {
        return BigDecimal.valueOf(result);
    }
}