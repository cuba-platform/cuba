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
public class DatatypeRange extends AbstractRange implements Range {

    private final Datatype datatype;

    public DatatypeRange(Datatype datatype) {
        this.datatype = datatype;
    }

    @Override
    public MetaClass asClass() {
        throw new IllegalStateException("Range is datatype");
    }

    @Override
    public Datatype asDatatype() {
        return datatype;
    }

    @Override
    public Enumeration asEnumeration() {
        throw new IllegalStateException("Range is datatype");
    }

    @Override
    public boolean isClass() {
        return false;
    }

    @Override
    public boolean isDatatype() {
        return true;
    }

    @Override
    public boolean isEnum() {
        return false;
    }

    @Override
    public String toString() {
        return "Range{datatype=" + datatype + "}";
    }
}