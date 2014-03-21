/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.gui.components.PickerField;
import com.vaadin.data.Property;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.event.Action;
import com.vaadin.event.FieldEvents;
import com.vaadin.server.AbstractErrorMessage;
import com.vaadin.server.CompositeErrorMessage;
import com.vaadin.server.ErrorMessage;
import com.vaadin.ui.*;
import org.apache.commons.lang.StringUtils;

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
    protected CubaHorizontalActionsLayout container;

    protected boolean suppressTextChangeListener = false;

    public CubaPickerField() {
        initTextField();
        initLayout();
        setValidationVisible(false);
    }

    public CubaPickerField(com.vaadin.ui.AbstractField field) {
        this.field = field;
        initLayout();
        setValidationVisible(false);
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

        setPrimaryStyleName("cuba-pickerfield");
        setWidth(DEFAULT_WIDTH + "px");
    }

    protected void initTextField() {
        field = new CubaPickerTextField();

        field.setImmediate(true);
        field.setReadOnly(true);
        ((TextField) field).setNullRepresentation("");
        addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (!suppressTextChangeListener) {
                    updateTextRepresentaion();
                }
            }
        });
    }

    @Override
    public void attach() {
        suppressTextChangeListener = true;

        super.attach();

        suppressTextChangeListener = false;

        // update text representaion manually
        if (field instanceof TextField) {
            updateTextRepresentaion();
        }
    }

    protected void updateTextRepresentaion() {
        TextField textField = (TextField) field;

        boolean textFieldReadonly = textField.isReadOnly();

        suppressTextChangeListener = true;

        textField.setReadOnly(false);
        textField.setValue(getStringRepresentaion());
        textField.setReadOnly(textFieldReadonly);

        suppressTextChangeListener = false;
    }

    protected String getStringRepresentaion() {
        if (captionFormatter != null) {
            return (String) captionFormatter.convertToPresentation(getValue(), String.class, getLocale());
        }

        return String.valueOf(getValue());
    }

    public List<Button> getButtons() {
        return Collections.unmodifiableList(buttons);
    }

    public void replaceButton(Button oldButton, Button button) {
        button.setTabIndex(-1);

        buttons.add(buttons.indexOf(oldButton), button);
        buttons.remove(oldButton);

        container.replaceComponent(oldButton, button);
        container.setComponentAlignment(button, Alignment.BOTTOM_LEFT);
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

    public void addFieldListener(final PickerField.FieldListener listener) {
        ((TextField) field).addTextChangeListener(new FieldEvents.TextChangeListener() {
            @Override
            public void textChange(FieldEvents.TextChangeEvent event) {
                if (!suppressTextChangeListener && !StringUtils.equals(getStringRepresentaion(), event.getText())) {
                    suppressTextChangeListener = true;

                    listener.actionPerformed(event.getText(), getValue());

                    suppressTextChangeListener = false;

                    // update text representaion manually
                    if (field instanceof TextField) {
                        updateTextRepresentaion();
                    }
                }
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

    @Override
    public ErrorMessage getErrorMessage() {
        ErrorMessage superError = super.getErrorMessage();
        if (isRequired() && isEmpty()) {

            ErrorMessage error = AbstractErrorMessage.getErrorMessageForException(
                    new com.vaadin.data.Validator.EmptyValueException(getRequiredError()));
            if (error != null) {
                return new CompositeErrorMessage(superError, error);
            }
        }

        return superError;
    }

    public Converter getCaptionFormatter() {
        return captionFormatter;
    }

    public void setCaptionFormatter(Converter captionFormatter) {
        this.captionFormatter = captionFormatter;
    }
}