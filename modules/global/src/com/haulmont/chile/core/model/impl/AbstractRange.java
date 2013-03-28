/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.chile.core.model.impl;

/**
 * @author abramov
 * @version $Id$
 */
import com.haulmont.chile.core.model.Range;

public abstract class AbstractRange implements Range {

	private Cardinality cardinality = Cardinality.NONE;
	private boolean ordered;

	public AbstractRange() {
		super();
	}

	public Cardinality getCardinality() {
		return cardinality;
	}

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