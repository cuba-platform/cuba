/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.gui.components.ProgressBar;
import com.haulmont.cuba.gui.data.ValueChangingListener;
import com.haulmont.cuba.gui.data.ValueListener;
import org.apache.commons.lang.ObjectUtils;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Desktop implementation of progress bar depending on swing JProgressBar component.
 *
 * @author Alexander Budarov
 * @version $Id$
 */
public class DesktopProgressBar extends DesktopAbstractComponent<JProgressBar> implements ProgressBar {

    protected boolean editable = true;
    protected List<ValueListener> listeners = new ArrayList<ValueListener>();
    protected Object prevValue;

    private static final int WHOLE_PROGRESS = 100;

    public DesktopProgressBar() {
        impl = new JProgressBar();
        impl.setMinimum(0);
        impl.setMaximum(WHOLE_PROGRESS);
    }

    @Override
    public void addListener(ValueListener listener) {
        if (!listeners.contains(listener))
            listeners.add(listener);
    }

    @Override
    public void removeListener(ValueListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void setValueChangingListener(ValueChangingListener listener) {
    }

    @Override
    public void removeValueChangingListener() {
    }

    protected void fireValueChanged(Object prevValue, Object value) {
        for (ValueListener listener : listeners) {
            listener.valueChanged(this, "value", prevValue, value);
        }
    }

    @Override
    public boolean isIndeterminate() {
        return impl.isIndeterminate();
    }

    @Override
    public void setIndeterminate(boolean indeterminate) {
        impl.setIndeterminate(indeterminate);
    }

    @Override
    public <T> T getValue() {
        Float value = convertValueFromSwing(impl.getValue());
        return (T) value;
    }

    @Override
    public void setValue(Object value) {
        if (!ObjectUtils.equals(prevValue, value)) {
            updateComponent(value);
            fireChangeListeners(value);
        }
    }

    private void fireChangeListeners(Object newValue) {
        Object oldValue = prevValue;
        prevValue = newValue;
        if (!ObjectUtils.equals(oldValue, newValue)) {
            fireValueChanged(oldValue, newValue);
        }
    }

    private void updateComponent(Object value) {
        float floatValue = value != null ? ((Number) value).floatValue() : 0;
        int progress = convertValueToSwing(floatValue);
        impl.setValue(progress);
    }

    @Override
    public boolean isEditable() {
        return editable;
    }

    @Override
    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    protected int convertValueToSwing(float progress) {
        return Math.round(progress * WHOLE_PROGRESS);
    }

    protected float convertValueFromSwing(int progress) {
        return (float) progress / (float) WHOLE_PROGRESS;
    }
}