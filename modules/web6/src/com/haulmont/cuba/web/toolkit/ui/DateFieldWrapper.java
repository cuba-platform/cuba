/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.web.gui.components.WebDateField;
import com.vaadin.data.Property;
import com.vaadin.ui.Layout;
import org.apache.commons.lang.ObjectUtils;

import java.util.Date;

/**
 * @author devyatkin
 * @version $Id$
 */
public class DateFieldWrapper extends CustomField {

    private WebDateField dateField;

    public DateFieldWrapper(WebDateField dateField, Layout composition) {
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
    public void setValue(Object newValue) throws ReadOnlyException, ConversionException {
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
        Object newValue = newDataSource != null ? newDataSource.getValue() : null;
        Object oldValue = getValue();

        super.setPropertyDataSource(newDataSource);
        // support dateField in editable table
        if (newDataSource != null && !ObjectUtils.equals(newValue, oldValue))
            dateField.setValue(newValue);
    }
}