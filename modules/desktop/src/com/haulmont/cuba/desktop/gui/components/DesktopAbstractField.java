/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.data.ValueListener;

import javax.swing.*;
import java.util.ArrayList;
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
    }

    @Override
    public void removeValidator(Validator validator) {
    }

    @Override
    public boolean isValid() {
        return true;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public void setRequiredMessage(String msg) {
    }

}
