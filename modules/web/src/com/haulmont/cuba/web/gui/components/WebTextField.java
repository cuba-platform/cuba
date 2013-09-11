/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Formatter;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.web.toolkit.ui.CubaTextField;

/**
 * @author abramov
 * @version $Id$
 */
public class WebTextField
        extends
            WebAbstractTextField<CubaTextField>
        implements
            TextField, Component.Wrapper {

    protected Datatype datatype;
    protected Formatter formatter;

    protected boolean trimming = true;

    @Override
    protected CubaTextField createTextFieldImpl() {
        return new CubaTextField();
    }

    @Override
    public Datatype getDatatype() {
        return datatype;
    }

    @Override
    public void setDatatype(Datatype datatype) {
        this.datatype = datatype;
        if (datatype == null) {
            initFieldConverter();
        } else {
            component.setConverter(new TextFieldStringToDatatypeConverter(datatype));
        }
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
    public int getMaxLength() {
        return component.getMaxLength();
    }

    @Override
    public void setMaxLength(int value) {
        component.setMaxLength(value);
    }

    @Override
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
    public boolean isTrimming() {
        return trimming;
    }

    @Override
    public void setTrimming(boolean trimming) {
        this.trimming = trimming;
    }
}