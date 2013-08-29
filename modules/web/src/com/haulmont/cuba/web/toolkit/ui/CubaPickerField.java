/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.web.toolkit.ui.converters.StringToEntityConverter;
import com.vaadin.data.Property;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.event.Action;
import com.vaadin.event.FieldEvents;
import com.vaadin.ui.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author krivopustov
 * @version $Id$
 */
public class CubaPickerField extends com.vaadin.ui.CustomField implements Action.Container {

    public static final int DEFAULT_WIDTH = 250;

    protected com.vaadin.ui.AbstractField field;

    protected List<Button> buttons = new ArrayList<>();
    private CubaHorizontalActionsLayout container;

    public CubaPickerField() {
        initTextField();
        initLayout();
    }

    public CubaPickerField(com.vaadin.ui.AbstractField field) {
        this.field = field;
        initLayout();
    }

    @Override
    protected boolean isEmpty() {
        return getValue() == null;
    }

    @Override
    protected Component initContent() {
        return container;
    }

    protected void initLayout() {
        container = new CubaHorizontalActionsLayout();
        container.setWidth("100%");

        container.addComponent(field);
        container.setComponentAlignment(field, Alignment.BOTTOM_LEFT);
        field.setWidth("100%");
        container.setExpandRatio(field, 1);

        setStyleName("cuba-pickerfield");
        setWidth(DEFAULT_WIDTH + "px");
    }

    protected void initTextField() {
        field = new TextField() {
            @Override
            public boolean isRequired() {
                return CubaPickerField.this.isRequired();
            }

            @Override
            public String getRequiredError() {
                return CubaPickerField.this.getRequiredError();
            }

            @Override
            public void setPropertyDataSource(Property newDataSource) {
                super.setPropertyDataSource(newDataSource);
//                vaadin7 converters
                setConverter(new StringToEntityConverter());
            }
        };
        field.setImmediate(true);
        field.setReadOnly(true);
        field.setConverter(new StringToEntityConverter());
        ((TextField) field).setNullRepresentation("");
//        vaadin7
//        ((TextField) field).setAllowFocusReadonly(true);
    }

    public List<Button> getButtons() {
        return Collections.unmodifiableList(buttons);
    }

    public void addButton(Button button) {
        button.setTabIndex(-1);
        buttons.add(button);
        container.addComponent(button);
        container.setComponentAlignment(button, Alignment.BOTTOM_LEFT);
    }

    public void removeButton(Button button) {
        buttons.remove(button);
        container.removeComponent(button);
    }

    public AbstractField getField() {
        return field;
    }

    public void addFieldListener(final com.haulmont.cuba.gui.components.PickerField.FieldListener listener) {
        ((TextField) field).addTextChangeListener(new FieldEvents.TextChangeListener() {
            @Override
            public void textChange(FieldEvents.TextChangeEvent event) {
                if (getValue() != null && event.getText().equals(getValue().toString()))
                    return;
                listener.actionPerformed(event.getText(), getValue());
            }
        });
    }

    @Override
    public Object getValue() {
        Property property = getPropertyDataSource();
        if (property != null) {
            return property.getValue();
        }
        return field.getValue();
    }

    @Override
    public void setValue(Object newValue) throws ReadOnlyException, Converter.ConversionException {
        boolean fieldReadOnly = field.isReadOnly();
        field.setReadOnly(false);
        getPropertyDataSource().setValue(newValue);
        field.setReadOnly(fieldReadOnly);
    }

    @Override
    public void addValueChangeListener(ValueChangeListener listener) {
        field.addValueChangeListener(listener);
    }

    @Override
    public void removeValueChangeListener(ValueChangeListener listener) {
        field.removeValueChangeListener(listener);
    }

    @Override
    public void valueChange(Property.ValueChangeEvent event) {
        field.valueChange(event);
    }

    @Override
    public Class getType() {
        return field.getType();
    }

    @Override
    public Property getPropertyDataSource() {
        return field.getPropertyDataSource();
    }

    @Override
    public void setPropertyDataSource(Property newDataSource) {
        field.setPropertyDataSource(newDataSource);
    }

    @Override
    public void focus() {
        field.focus();
    }

    @Override
    public void setBuffered(boolean buffered) {
        field.setBuffered(buffered);
    }

    @Override
    public boolean isBuffered() {
        return field.isBuffered();
    }

    @Override
    public void removeAllValidators() {
        field.removeAllValidators();
    }

    @Override
    public void addActionHandler(Action.Handler actionHandler) {
        container.addActionHandler(actionHandler);
    }

    @Override
    public void removeActionHandler(Action.Handler actionHandler) {
        container.removeActionHandler(actionHandler);
    }

}