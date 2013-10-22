/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Formatter;
import com.haulmont.cuba.gui.components.TextField;

import javax.annotation.Nullable;

/**
 * @author abramov
 * @version $Id$
 */
public class WebTextField
    extends
        WebAbstractTextField<com.haulmont.cuba.web.toolkit.ui.TextField>
    implements
        TextField, Component.Wrapper {

    protected Datatype datatype;

    protected boolean trimming = true;

    @Override
    protected com.haulmont.cuba.web.toolkit.ui.TextField createTextFieldImpl() {
        return new com.haulmont.cuba.web.toolkit.ui.TextField();
    }

    @Override
    public Formatter getFormatter() {
        return formatter;
    }

    @Override
    public void setFormatter(Formatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public boolean isTrimming() {
        return trimming;
    }

    @Override
    public void setTrimming(boolean trimming) {
        this.trimming = trimming;
    }

    @Override
    public int getMaxLength() {
        return component.getMaxLength();
    }

    @Override
    public void setMaxLength(int value) {
        component.setMaxLength(value);
    }

    @Override
    public Datatype getDatatype() {
        return datatype;
    }

    @Override
    @Nullable
    protected Datatype getActualDatatype() {
        if (metaProperty != null) {
            return metaProperty.getRange().isDatatype() ? metaProperty.getRange().asDatatype() : null;
        } else if (datatype != null) {
            return datatype;
        } else {
            return Datatypes.getNN(String.class);
        }
    }

    @Override
    public void setDatatype(Datatype datatype) {
        this.datatype = datatype;
    }
}