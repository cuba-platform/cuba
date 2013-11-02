/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.PasswordField;
import com.vaadin.server.AbstractErrorMessage;
import com.vaadin.server.CompositeErrorMessage;
import com.vaadin.server.ErrorMessage;

/**
 * @author artamonov
 * @version $Id$
 */
public class WebPasswordField extends WebAbstractTextField<com.vaadin.ui.PasswordField> implements PasswordField {

    @Override
    protected com.vaadin.ui.PasswordField createTextFieldImpl() {
        return new com.vaadin.ui.PasswordField() {
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
        };
    }

    @Override
    public int getMaxLength() {
        return component.getMaxLength();
    }

    @Override
    public void setMaxLength(int value) {
        component.setMaxLength(value);
    }
}