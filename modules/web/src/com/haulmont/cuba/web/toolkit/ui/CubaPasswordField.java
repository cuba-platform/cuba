/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.vaadin.server.AbstractErrorMessage;
import com.vaadin.server.CompositeErrorMessage;
import com.vaadin.server.ErrorMessage;
import com.vaadin.ui.PasswordField;
import com.haulmont.cuba.web.toolkit.ui.client.passwordfield.CubaPasswordFieldState;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaPasswordField extends PasswordField {

    public CubaPasswordField() {
        setValidationVisible(false);
        setShowBufferedSourceException(false);
        setShowErrorForDisabledState(false);
        setAutocomplete(false);
    }

    @Override
    protected CubaPasswordFieldState getState() {
        return (CubaPasswordFieldState) super.getState();
    }

    @Override
    protected CubaPasswordFieldState getState(boolean markAsDirty) {
        return (CubaPasswordFieldState) super.getState(markAsDirty);
    }

    public boolean isAutocomplete() {
        return getState(false).autocomplete;
    }

    public void setAutocomplete(boolean autocomplete) {
        if (isAutocomplete() != autocomplete) {
            getState().autocomplete = autocomplete;
        }
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
}