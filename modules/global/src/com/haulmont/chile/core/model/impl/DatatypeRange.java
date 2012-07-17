package com.haulmont.chile.core.model.impl;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Enumeration;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.Range;

public class DatatypeRange extends AbstractRange implements Range {
	private final Datatype datatype;
	public DatatypeRange(Datatype datatype) {
		this.datatype = datatype;
	}

	public MetaClass asClass() {
		throw new IllegalStateException("Range is datatype");
	}

	public Datatype asDatatype() {
		return datatype;
	}

	public Enumeration asEnumeration() {
		throw new IllegalStateException("Range is datatype");
	}

	public boolean isClass() {
		return false;
	}

	public boolean isDatatype() {
		return true;
	}

	public boolean isEnum() {
		return false;
	}

    @Override
    public String toString() {
        return "Range{datatype=" + datatype + "}";
    }
}
