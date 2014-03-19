/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.chile.core.model.impl;

import com.haulmont.chile.core.model.Range;

/**
 * @author abramov
 * @version $Id$
 */
public abstract class AbstractRange implements Range {

    private Cardinality cardinality = Cardinality.NONE;
    private boolean ordered;

    public AbstractRange() {
        super();
    }

    @Override
    public Cardinality getCardinality() {
        return cardinality;
    }

    @Override
    public boolean isOrdered() {
        return ordered;
    }

    public void setCardinality(Cardinality cardinality) {
        this.cardinality = cardinality;
    }

    public void setOrdered(boolean ordered) {
        this.ordered = ordered;
    }
}