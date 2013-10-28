/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.vaadin.event.FieldEvents;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author krivopustov
 * @version $Id$
 */
public class PickerField extends CustomField {

    public static final int DEFAULT_WIDTH = 250;

    protected com.vaadin.ui.AbstractField field;

    protected boolean required;
    protected String requiredError;

    protected List<Button> buttons = new ArrayList<>();
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
        container.setComponentAlignment(field, Alignment.BOTTOM_LEFT);
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
        ((TextField) field).setAllowFocusReadonly(true);
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
        ((TextField) field).addListener(new FieldEvents.TextChangeListener() {
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
        return field.getValue();
    }

    @Override
    public void setValue(Object newValue) throws ReadOnlyException, ConversionException {
        boolean fieldReadOnly = field.isReadOnly();
        field.setReadOnly(false);
        field.setValue(newValue);
        field.setReadOnly(fieldReadOnly);
    }

    @Override
    public Class getType() {
        return field.getType();
    }

    @Override
    public void focus() {
        field.focus();
    }
}