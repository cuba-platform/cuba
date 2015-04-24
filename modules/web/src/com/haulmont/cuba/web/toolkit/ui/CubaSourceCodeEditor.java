/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.gui.components.autocomplete.AutoCompleteSupport;
import com.vaadin.server.AbstractErrorMessage;
import com.vaadin.server.CompositeErrorMessage;
import com.vaadin.server.ErrorMessage;
import com.vaadin.ui.AbstractTextField;
import org.vaadin.aceeditor.AceEditor;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaSourceCodeEditor extends AceEditor implements AutoCompleteSupport {

    public CubaSourceCodeEditor() {
        String aceLocation = "VAADIN/resources/ace";

        setBasePath(aceLocation);
        setThemePath(aceLocation);
        setWorkerPath(aceLocation);
        setModePath(aceLocation);

        setUseWorker(false);

        setTextChangeEventMode(AbstractTextField.TextChangeEventMode.LAZY);
        setTextChangeTimeout(200);

        setValidationVisible(false);
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