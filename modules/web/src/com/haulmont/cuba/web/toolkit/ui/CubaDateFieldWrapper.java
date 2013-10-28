/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.web.gui.components.WebDateField;
import com.haulmont.cuba.web.toolkit.ui.converters.ObjectToObjectConverter;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.ui.Component;
import com.vaadin.ui.Layout;

import java.util.Date;

/**
 * @author devyatkin
 * @version $Id$
 */
public class CubaDateFieldWrapper extends com.vaadin.ui.CustomField {

    protected final Layout composition;
    protected WebDateField dateField;

    public CubaDateFieldWrapper(WebDateField dateField, Layout composition) {
        this.dateField = dateField;
        this.composition = composition;

        this.composition.setSizeUndefined();

        setSizeUndefined();
        setConverter(new ObjectToObjectConverter());
    }

    @Override
    protected Component initContent() {
        return composition;
    }

    public WebDateField getCubaField() {
        return dateField;
    }

    @Override
    public Object getValue() {
        return dateField.getValue();
    }

    @Override
    public void setValue(Object newValue) throws ReadOnlyException, Converter.ConversionException {
        dateField.setValue(newValue);
    }

    @Override
    protected boolean isEmpty() {
        return getValue() == null;
    }

    @Override
    public void focus() {
        dateField.getDateField().focus();
    }

    @Override
    public Class<?> getType() {
        return Date.class;
    }

    @Override
    public void setWidth(float width, Unit unit) {
        super.setWidth(width, unit);

        if (composition != null) {
            if (width < 0) {
                composition.setWidth(-1, Unit.PIXELS);
            } else {
                composition.setWidth(100, Unit.PERCENTAGE);
            }
        }
    }

    @Override
    public void setHeight(float height, Unit unit) {
        super.setHeight(height, unit);

        if (composition != null) {
            if (height < 0) {
                composition.setHeight(-1, Unit.PIXELS);
            } else {
                composition.setHeight(100, Unit.PERCENTAGE);
            }
        }
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        dateField.setEditable(!readOnly);
    }

    @Override
    public boolean isReadOnly() {
        return !dateField.isEditable();
    }

    public void setCompositionReadOnly(boolean readOnly) {
        super.setReadOnly(readOnly);
    }
}