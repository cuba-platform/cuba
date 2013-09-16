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
public class ClassRange extends AbstractRange implements Range {
	private final MetaClass metaClass;

	public ClassRange(MetaClass metaClass) {
		this.metaClass = metaClass;
	}

    @Override
	public MetaClass asClass() {
		return metaClass;
	}

    @Override
	public Datatype asDatatype() {
		throw new IllegalStateException("Range is class");
	}

    @Override
	public Enumeration asEnumeration() {
		throw new IllegalStateException("Range is class");
	}

    @Override
	public boolean isClass() {
		return true;
	}

    @Override
	public boolean isDatatype() {
		return false;
	}

    @Override
	public boolean isEnum() {
		return false;
	}

    @Override
    public String toString() {
        return "Range{metaClass=" + metaClass + "}";
    }
}