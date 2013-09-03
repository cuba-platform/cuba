/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui;

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
    protected Converter captionFormatter;

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
        field = new TextField();

        field.setImmediate(true);
        field.setReadOnly(true);
        ((TextField) field).setNullRepresentation("");
//        vaadin7
//        ((TextField) field).setAllowFocusReadonly(true);
        addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                TextField textField = (TextField) field;
                Property property = event.getProperty();

                boolean textFieldReadonly = textField.isReadOnly();
                textField.setReadOnly(false);
                if (captionFormatter != null) {
                    Object captionValue = captionFormatter.convertToPresentation(getValue(), String.class, getLocale());
                    textField.setValue((String) captionValue);
                } else {
                    textField.setValue(String.valueOf(property.getValue()));
                }
                textField.setReadOnly(textFieldReadonly);
            }
        });
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
    public Class getType() {
        return Object.class;
    }

    @Override
    public void focus() {
        field.focus();
    }

    @Override
    public void addActionHandler(Action.Handler actionHandler) {
        container.addActionHandler(actionHandler);
    }

    @Override
    public void removeActionHandler(Action.Handler actionHandler) {
        container.removeActionHandler(actionHandler);
    }

    public Converter getCaptionFormatter() {
        return captionFormatter;
    }

    public void setCaptionFormatter(Converter captionFormatter) {
        this.captionFormatter = captionFormatter;
    }
}