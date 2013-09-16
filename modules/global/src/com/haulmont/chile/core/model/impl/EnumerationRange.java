/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.chile.core.model.impl;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Enumeration;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.Range;

/**
 * @author krivopustov
 * @version $Id$
 */
public class EnumerationRange extends AbstractRange implements Range {

	private Enumeration enumeration;

    public EnumerationRange(Enumeration enumeration) {
		super();
		this.enumeration = enumeration;
	}

    @Override
    public MetaClass asClass() {
		throw new IllegalStateException("Range is enumeration");
	}

    @Override
	public Datatype asDatatype() {
		throw new IllegalStateException("Range is enumeration");
	}

    @Override
	public Enumeration asEnumeration() {
		return enumeration;
	}

    @Override
	public boolean isClass() {
		return false;
	}

    @Override
	public boolean isDatatype() {
		return false;
	}

    @Override
	public boolean isEnum() {
		return true;
	}

    @Override
    public String toString() {
        return "Range{enum=" + enumeration + "}";
    }
}