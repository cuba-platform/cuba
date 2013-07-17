/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
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