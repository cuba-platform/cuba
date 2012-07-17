package com.haulmont.chile.core.model.impl;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Enumeration;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.Range;

public class EnumerationRange extends AbstractRange implements Range {

	private Enumeration enumeration;

    public EnumerationRange(Enumeration enumeration) {
		super();
		this.enumeration = enumeration;
	}

    public MetaClass asClass() {
		throw new IllegalStateException("Range is enumeration");
	}

	public Datatype asDatatype() {
		throw new IllegalStateException("Range is enumeration");
	}

	public Enumeration asEnumeration() {
		return enumeration;
	}

	public boolean isClass() {
		return false;
	}

	public boolean isDatatype() {
		return false;
	}

	public boolean isEnum() {
		return true;
	}

    @Override
    public String toString() {
        return "Range{enum=" + enumeration + "}";
    }
}
