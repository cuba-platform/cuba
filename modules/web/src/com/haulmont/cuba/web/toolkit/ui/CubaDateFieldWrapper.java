/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.web.gui.components.WebDateField;
import com.haulmont.cuba.web.toolkit.ui.converters.ObjectToObjectConverter;
import com.vaadin.data.Property;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.ui.Component;
import com.vaadin.ui.Layout;
import org.apache.commons.lang.ObjectUtils;

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

        this.composition.setWidth("100%");

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
        if (getPropertyDataSource() != null)
            return getPropertyDataSource().getValue();
        return dateField.getValue();
    }

    @Override
    public void setValue(Object newValue) throws ReadOnlyException, Converter.ConversionException {
        if (getPropertyDataSource() != null)
            getPropertyDataSource().setValue(newValue);
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
    public void setReadOnly(boolean readOnly) {
        dateField.setEditable(!readOnly);
    }

    @Override
    public boolean isReadOnly() {
        return !dateField.isEditable();
    }

    @Override
    public void valueChange(Property.ValueChangeEvent event) {
        super.valueChange(event);
        // support dateField in editable table
        Property property = event.getProperty();
        if (property != null && !ObjectUtils.equals(property.getValue(), getValue()))
            dateField.setValue(property.getValue());
    }

    @Override
    public void setPropertyDataSource(Property newDataSource) {
        Object newValue = newDataSource != null ? newDataSource.getValue() : null;
        Object oldValue = getValue();

        super.setPropertyDataSource(newDataSource);
        // support dateField in editable table
        if (newDataSource != null && !ObjectUtils.equals(newValue, oldValue))
            dateField.setValue(newValue);
    }
}