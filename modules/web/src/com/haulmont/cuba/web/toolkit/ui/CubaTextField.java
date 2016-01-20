/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.web.toolkit.ui.client.textfield.CubaTextFieldState;
import com.vaadin.event.Action;
import com.vaadin.event.ActionManager;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.*;
import com.vaadin.ui.TextField;

import java.util.Map;

/**
 * @author devyatkin
 * @version $Id$
 */
public class CubaTextField extends TextField implements Action.Container {

    /**
     * Keeps track of the Actions added to this component, and manages the
     * painting and handling as well.
     */
    protected ActionManager shortcutsManager;

    public CubaTextField() {
        setValidationVisible(false);
        setShowBufferedSourceException(false);
        setShowErrorForDisabledState(false);
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
    public void paintContent(PaintTarget target) throws PaintException {
        super.paintContent(target);

        if (shortcutsManager != null) {
            shortcutsManager.paintActions(null, target);
        }
    }

    @Override
    protected ActionManager getActionManager() {
        if (shortcutsManager == null) {
            shortcutsManager = new ActionManager(this);
        }
        return shortcutsManager;
    }

    @Override
    public void changeVariables(Object source, Map<String, Object> variables) {
        super.changeVariables(source, variables);

        // Actions
        if (shortcutsManager != null) {
            shortcutsManager.handleActions(variables, this);
        }
    }

    @Override
    public void addShortcutListener(ShortcutListener listener) {
        getActionManager().addAction(listener);
    }

    @Override
    public void removeShortcutListener(ShortcutListener listener) {
        getActionManager().removeAction(listener);
    }

    @Override
    public void addActionHandler(Action.Handler actionHandler) {
        getActionManager().addActionHandler(actionHandler);
    }

    @Override
    public void removeActionHandler(Action.Handler actionHandler) {
        getActionManager().removeActionHandler(actionHandler);
    }

    @Override
    protected CubaTextFieldState getState() {
        return (CubaTextFieldState) super.getState();
    }

    @Override
    protected CubaTextFieldState getState(boolean markAsDirty) {
        return (CubaTextFieldState) super.getState(markAsDirty);
    }

    /**
     * Sets whether a text field will be focusable in readOnly mode
     */
    public void setReadOnlyFocusable(boolean readOnlyFocusable) {
        getState(true).readOnlyFocusable = readOnlyFocusable;
    }

    public boolean isReadOnlyFocusable() {
        return getState(false).readOnlyFocusable;
    }

}