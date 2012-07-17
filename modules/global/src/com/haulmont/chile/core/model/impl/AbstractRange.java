package com.haulmont.chile.core.model.impl;

import com.haulmont.chile.core.model.Range.Cardinality;
import com.haulmont.chile.core.model.Range;

public abstract class AbstractRange implements Range {

	private Cardinality cardinality;
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