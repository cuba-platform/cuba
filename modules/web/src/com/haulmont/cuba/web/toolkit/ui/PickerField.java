/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.vaadin.data.Property;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.*;
import com.vaadin.ui.TextField;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class PickerField extends CustomComponent implements com.vaadin.ui.Field  {

    public static final int DEFAULT_WIDTH = 250;

    protected com.vaadin.ui.AbstractField field;

    protected boolean required;
    protected String requiredError;

    protected List<Button> buttons = new ArrayList<Button>();
    private HorizontalLayout container;

    public PickerField() {
        initTextField();
        initLayout();
    }

    public PickerField(com.vaadin.ui.AbstractField field) {
        this.field = field;
        initLayout();
    }

    protected void initLayout() {
        container = new HorizontalLayout();
        container.setWidth("100%");

        container.addComponent(field);
        field.setWidth("100%");
        container.setExpandRatio(field, 1);

        setCompositionRoot(container);
        setStyleName("pickerfield");
        setWidth(DEFAULT_WIDTH + "px");
    }

    protected void initTextField() {
        field = new TextField() {
            @Override
            public boolean isRequired() {
                return PickerField.this.required;
            }

            @Override
            public String getRequiredError() {
                return PickerField.this.requiredError;
            }
        };
        field.setReadOnly(true);
        ((TextField) field).setNullRepresentation("");
    }

    public List<Button> getButtons() {
        return Collections.unmodifiableList(buttons);
    }

    public void addButton(Button button) {
        buttons.add(button);
        container.addComponent(button);
    }

    public void removeButton(Button button) {
        buttons.remove(button);
        container.removeComponent(button);
    }

    public AbstractField getField() {
        return field;
    }

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        paintCommonContent(target);
        super.paintContent(target);
    }

    protected void paintCommonContent(PaintTarget target) throws PaintException {
        // If the field is modified, but not committed, set modified attribute
        if (isModified()) {
            target.addAttribute("modified", true);
        }

        // Adds the required attribute
        if (!isReadOnly() && isRequired()) {
            target.addAttribute("required", true);
        }

        // Hide the error indicator if needed
        if (isRequired() && getValue() == null && getComponentError() == null
                && getErrorMessage() != null) {
            target.addAttribute("hideErrors", true);
        }
    }

    public boolean isInvalidCommitted() {
        return field.isInvalidCommitted();
    }

    public void setInvalidCommitted(boolean isCommitted) {
        field.setInvalidCommitted(isCommitted);
    }

    public void commit() throws SourceException, com.vaadin.data.Validator.InvalidValueException {
        field.commit();
    }

    public void discard() throws SourceException {
        field.discard();
    }

    public boolean isModified() {
        return field.isModified();
    }

    public boolean isWriteThrough() {
        return field.isWriteThrough();
    }

    public void setWriteThrough(boolean writeTrough) throws SourceException, com.vaadin.data.Validator.InvalidValueException {
        field.setWriteThrough(writeTrough);
    }

    public boolean isReadThrough() {
        return field.isReadThrough();
    }

    public void setReadThrough(boolean readTrough) throws SourceException {
        field.setReadThrough(readTrough);
    }

    public Object getValue() {
        Property property = getPropertyDataSource();
        if (property != null) {
            return property.getValue();
        }
        return field.getValue();
    }

    public void setValue(Object newValue) throws ReadOnlyException, ConversionException {
        boolean readOnly = field.isReadOnly();
        field.setReadOnly(false);
        field.setValue(newValue);
        field.setReadOnly(readOnly);
    }

    public Class getType() {
        return field.getType();
    }

    public Property getPropertyDataSource() {
        return field.getPropertyDataSource();
    }

    public void setPropertyDataSource(Property newDataSource) {
        field.setPropertyDataSource(newDataSource);
    }

    public void addValidator(com.vaadin.data.Validator validator) {
        field.addValidator(validator);
    }

    public Collection getValidators() {
        return field.getValidators();
    }

    public void removeValidator(com.vaadin.data.Validator validator) {
        field.removeValidator(validator);
    }

    public boolean isValid() {
        return field.isValid();
    }

    public void validate() throws com.vaadin.data.Validator.InvalidValueException {
        if (field.getValue() == null) {
            if (isRequired()) {
                throw new com.vaadin.data.Validator.EmptyValueException(requiredError);
            } else {
                return;
            }
        }
        field.validate();
    }

    public boolean isInvalidAllowed() {
        return field.isInvalidAllowed();
    }

    public void setInvalidAllowed(boolean invalidAllowed) throws UnsupportedOperationException {
        field.setInvalidAllowed(invalidAllowed);
    }

    public void addListener(ValueChangeListener listener) {
        field.addListener(listener);
    }

    public void removeListener(ValueChangeListener listener) {
        field.removeListener(listener);
    }

    public void valueChange(Property.ValueChangeEvent event) {
        field.valueChange(event);
    }

    public void focus() {
        field.focus();
    }

    public int getTabIndex() {
        return field.getTabIndex();
    }

    public void setTabIndex(int tabIndex) {
        field.setTabIndex(tabIndex);
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
        requestRepaint();
    }

    public void setRequiredError(String requiredMessage) {
        this.requiredError = requiredMessage;
        requestRepaint();
    }

    public String getRequiredError() {
        return requiredError;
    }
}

