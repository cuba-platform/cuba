package com.haulmont.chile.core.model.impl;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Enumeration;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.Range;

public class ClassRange extends AbstractRange implements Range {
	private final MetaClass metaClass;

	public ClassRange(MetaClass metaClass) {
		this.metaClass = metaClass;
	}

	public MetaClass asClass() {
		return metaClass;
	}

	public Datatype asDatatype() {
		throw new IllegalStateException("Range is class");
	}

	public Enumeration asEnumeration() {
		throw new IllegalStateException("Range is class");
	}

	public boolean isClass() {
		return true;
	}

	public boolean isDatatype() {
		return false;
	}

	public boolean isEnum() {
		return false;
	}

    @Override
    public String toString() {
        return "Range{metaClass=" + metaClass + "}";
    }
}
