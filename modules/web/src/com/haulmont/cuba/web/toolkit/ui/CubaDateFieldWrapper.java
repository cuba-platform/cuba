/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.web.gui.components.WebDateField;
import com.vaadin.data.Property;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.ui.Layout;
import org.apache.commons.lang.ObjectUtils;

import java.util.Date;

/**
 * @author devyatkin
 * @version $Id$
 */
public class CubaDateFieldWrapper extends CustomField {

    private WebDateField dateField;

    public CubaDateFieldWrapper(WebDateField dateField, Layout composition) {
        this.dateField = dateField;
        composition.setWidth("100%");
        setSizeUndefined();
        setCompositionRoot(composition);
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
    public boolean isRequired() {
        return dateField.isRequired();
    }

    @Override
    public void setRequired(boolean required) {
        dateField.setRequired(required);
        super.setRequired(required);
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
        super.setPropertyDataSource(newDataSource);
        // support dateField in editable table
        if (newDataSource != null && !ObjectUtils.equals(newDataSource.getValue(), getValue()))
            dateField.setValue(newDataSource.getValue());
    }

    @Override
    public void setBuffered(boolean buffered) {
    }

    @Override
    public boolean isBuffered() {
        return false;
    }

    @Override
    public void removeAllValidators() {
    }
}
