/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.gui.components.autocomplete.AutoCompleteSupport;
import com.haulmont.cuba.web.toolkit.ui.client.sourcecodeeditor.CubaSourceCodeEditorState;
import com.vaadin.server.AbstractErrorMessage;
import com.vaadin.server.CompositeErrorMessage;
import com.vaadin.server.ErrorMessage;
import com.vaadin.ui.AbstractTextField;
import org.vaadin.aceeditor.AceEditor;

/**
 * @author artamonov
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
        setShowBufferedSourceException(false);
        setShowErrorForDisabledState(false);

        setHandleTabKey(false);
        setFontSize("auto");
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
    protected CubaSourceCodeEditorState getState() {
        return (CubaSourceCodeEditorState) super.getState();
    }

    @Override
    protected CubaSourceCodeEditorState getState(boolean markAsDirty) {
        return (CubaSourceCodeEditorState) super.getState(markAsDirty);
    }

    public void setHandleTabKey(boolean handleTabKey) {
        if (isHandleTabKey() != handleTabKey) {
            getState().handleTabKey = handleTabKey;
        }
    }

    public boolean isHandleTabKey() {
        return getState(false).handleTabKey;
    }
}