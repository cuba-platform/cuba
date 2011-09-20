/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.vaadin.data.Property;
import com.vaadin.data.util.AbstractProperty;
import com.vaadin.event.FieldEvents;
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
public class PickerField extends CustomField {

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
        field.setImmediate(true);
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

    public void addFieldListener(final com.haulmont.cuba.gui.components.PickerField.FieldListener listener) {
        ((TextField) field).addListener(new FieldEvents.TextChangeListener() {
            @Override
            public void textChange(FieldEvents.TextChangeEvent event) {
                if (getValue() != null && event.getText().equals(getValue().toString()))
                    return;
                listener.actionPerformed(event.getText(), getValue());
            }
        });
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

}

