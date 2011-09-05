/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.gui.components.ValidationException;
import com.haulmont.cuba.web.gui.components.WebDateField;
import com.vaadin.data.Property;
import com.vaadin.data.Validator;

import com.vaadin.ui.*;

import java.util.Collection;
import java.util.Date;

/**
 * <p>$Id$</p>
 *
 * @author devyatkin
 */
public class DateFieldWrapper extends CustomComponent implements Field {

    private WebDateField dateField;
    private int tabIndex;

    public DateFieldWrapper(WebDateField dateField) {
        this.dateField = dateField;
        dateField.getComposition().setWidth("100%");
        setSizeUndefined();
        setCompositionRoot(dateField.getComposition());
    }

    public WebDateField getCubaField() {
        return dateField;
    }

    @Override
    public Object getValue() {
        return dateField.getValue();
    }

    @Override
    public void setValue(Object newValue) throws ReadOnlyException, ConversionException {
        dateField.setValue(newValue);
    }

    public void focus() {
        dateField.requestFocus();
    }

    @Override
    public Class<?> getType() {
        return Date.class;
    }

    @Override
    public boolean isRequired() {
        return dateField.isRequired();
    }

    @Override
    public void setRequired(boolean required) {
        dateField.setRequired(required);
    }

    @Override
    public void setRequiredError(String requiredMessage) {
        dateField.setRequiredMessage(requiredMessage);
    }

    @Override
    public String getRequiredError() {
        return dateField.getRequiredMessage();
    }

    @Override
    public boolean isInvalidCommitted() {
        return true;
    }

    @Override
    public void setInvalidCommitted(boolean isCommitted) {
    }

    @Override
    public void commit() throws SourceException, Validator.InvalidValueException {
        System.out.println();
    }

    @Override
    public void discard() throws SourceException {
        System.out.println();
    }

    @Override
    public boolean isWriteThrough() {
        return false;
    }

    @Override
    public void setWriteThrough(boolean writeThrough) throws SourceException, Validator.InvalidValueException {
    }

    @Override
    public boolean isReadThrough() {
        return false;
    }

    @Override
    public void setReadThrough(boolean readThrough) throws SourceException {
    }

    @Override
    public boolean isModified() {
        return (dateField.getDateField().isModified() || dateField.getTimeField().<Field>getComponent().isModified());
    }

    @Override
    public int getTabIndex() {
        return tabIndex;
    }

    @Override
    public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
    }

    @Override
    public void addValidator(Validator validator) {
    }

    @Override
    public void removeValidator(Validator validator) {
    }

    @Override
    public Collection<Validator> getValidators() {
        System.out.println();
        return null;
    }

    @Override
    public boolean isValid() {
        return dateField.isValid();
    }

    @Override
    public void validate() throws Validator.InvalidValueException {
        try {
            dateField.validate();
        } catch (ValidationException e) {
            throw new Validator.InvalidValueException(e.getMessage());
        }
    }

    @Override
    public boolean isInvalidAllowed() {
        return true;
    }

    @Override
    public void setInvalidAllowed(boolean invalidValueAllowed) throws UnsupportedOperationException {
    }

    @Override
    public void valueChange(Property.ValueChangeEvent event) {
    }

    @Override
    public void addListener(ValueChangeListener listener) {
    }

    @Override
    public void removeListener(ValueChangeListener listener) {
    }

    @Override
    public void setPropertyDataSource(Property newDataSource) {
    }

    @Override
    public Property getPropertyDataSource() {
        return null;
    }
}
