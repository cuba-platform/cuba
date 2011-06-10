/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.RequiredValueMissingException;
import com.haulmont.cuba.gui.components.ValidationException;
import com.haulmont.cuba.gui.data.ValueListener;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public abstract class DesktopAbstractField<C extends JComponent>
        extends DesktopAbstractComponent<C>
        implements Field
{
    protected List<ValueListener> listeners = new ArrayList<ValueListener>();

    protected boolean required;
    protected String requiredMessage;

    protected Set<Validator> validators = new HashSet<Validator>();

    @Override
    public void addListener(ValueListener listener) {
        if (!listeners.contains(listener))
            listeners.add(listener);
    }

    @Override
    public void removeListener(ValueListener listener) {
        listeners.remove(listener);
    }

    protected void fireValueChanged(Object prevValue, Object value) {
        for (ValueListener listener : listeners) {
            listener.valueChanged(this, "value", prevValue, value);
        }
    }

    @Override
    public void addValidator(Validator validator) {
        validators.add(validator);
    }

    @Override
    public void removeValidator(Validator validator) {
        validators.remove(validator);
    }

    @Override
    public boolean isValid() {
        try {
            validate();
            return true;
        } catch (ValidationException e) {
            return false;
        }
    }

    protected boolean isEmpty(Object value) {
        return value == null;
    }

    @Override
    public void validate() throws ValidationException {
        Object value = getValue();
        if (isEmpty(value)) {
            if (isRequired())
                throw new RequiredValueMissingException(requiredMessage, this);
            else
                return;
        }

        for (Validator validator : validators) {
            validator.validate(value);
        }
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
        if (required)
            getImpl().setBackground(Color.yellow);
        else
            getImpl().setBackground(Color.white);
    }

    public void setRequiredMessage(String msg) {
        requiredMessage = msg;
    }
}
