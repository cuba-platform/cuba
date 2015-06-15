/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.gui.components.PickerField;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.web.App;
import com.vaadin.data.Property;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.event.Action;
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

    protected com.vaadin.ui.AbstractField field;
    protected Converter captionFormatter;

    protected List<Button> buttons = new ArrayList<>();
    protected CubaHorizontalActionsLayout container;

    protected boolean useCustomField = false;
    protected boolean fieldReadOnly = true;

    protected boolean suppressTextChangeListener = false;

    public CubaPickerField() {
        initTextField();
        initLayout();
        setValidationVisible(false);
        setShowBufferedSourceException(false);
    }

    public CubaPickerField(com.vaadin.ui.AbstractField field) {
        this.field = field;
        this.useCustomField = true;
        initLayout();
        setValidationVisible(false);
        setShowBufferedSourceException(false);
    }

    @Override
    protected Component initContent() {
        return container;
    }

    protected void initLayout() {
        container = new CubaHorizontalActionsLayout();
        container.setWidth("100%");

        field.setWidth("100%");
        container.addComponent(field);
        container.setExpandRatio(field, 1);

        setPrimaryStyleName("cuba-pickerfield");

        if (App.isBound()) {
            ThemeConstants theme = App.getInstance().getThemeConstants();
            setWidth(theme.get("cuba.web.CubaPickerField.width"));
        }
    }

    protected void initTextField() {
        field = new CubaTextField();
        ((CubaTextField) field).setReadOnlyFocusable(true);

        field.setImmediate(true);
        field.setReadOnly(true);
        ((TextField) field).setNullRepresentation("");
        addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (!suppressTextChangeListener) {
                    updateTextRepresentation();
                }
            }
        });
    }

    public boolean isFieldReadOnly() {
        return fieldReadOnly;
    }

    public void setFieldReadOnly(boolean fieldReadOnly) {
        this.fieldReadOnly = fieldReadOnly;

        if (!useCustomField) {
            getField().setReadOnly(isReadOnly() || fieldReadOnly);
        }
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        super.setReadOnly(readOnly);

        if (!useCustomField) {
            getField().setReadOnly(readOnly || fieldReadOnly);
        }
    }

    @Override
    public void attach() {
        suppressTextChangeListener = true;

        super.attach();

        suppressTextChangeListener = false;

        // update text representaion manually
        if (field instanceof TextField) {
            updateTextRepresentation();
        }
    }

    protected void updateTextRepresentation() {
        TextField textField = (TextField) field;

        suppressTextChangeListener = true;

        textField.setValueIgnoreReadOnly(getStringRepresentation());

        suppressTextChangeListener = false;
    }

    @SuppressWarnings("unchecked")
    protected String getStringRepresentation() {
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
    }

    public void addButton(Button button) {
        button.setTabIndex(-1);

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

    public void addFieldListener(final PickerField.FieldListener listener) {
        field.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                String text = (String) event.getProperty().getValue();

                if (!suppressTextChangeListener && !StringUtils.equals(getStringRepresentation(), text)) {
                    suppressTextChangeListener = true;

                    listener.actionPerformed(text, getValue());

                    suppressTextChangeListener = false;

                    // update text representaion manually
                    if (field instanceof TextField) {
                        updateTextRepresentation();
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
        if (!isReadOnly() && isRequired() && isEmpty()) {
            ErrorMessage error = AbstractErrorMessage.getErrorMessageForException(
                    new com.vaadin.data.Validator.EmptyValueException(getRequiredError()));
            if (error != null) {
                return new CompositeErrorMessage(superError, error);
            }
        }

        return superError;
    }

    @Override
    public boolean isEmpty() {
        return getValue() == null;
    }

    public Converter getCaptionFormatter() {
        return captionFormatter;
    }

    public void setCaptionFormatter(Converter captionFormatter) {
        this.captionFormatter = captionFormatter;
    }
}