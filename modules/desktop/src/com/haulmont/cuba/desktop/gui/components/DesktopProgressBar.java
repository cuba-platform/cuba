/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.desktop.gui.executors.impl.DesktopBackgroundWorker;
import com.haulmont.cuba.gui.components.ProgressBar;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.gui.components.compatibility.ComponentValueListenerWrapper;
import org.apache.commons.lang.ObjectUtils;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Desktop implementation of progress bar depending on swing JProgressBar component.
 */
public class DesktopProgressBar extends DesktopAbstractComponent<JProgressBar> implements ProgressBar {

    protected boolean editable = true;
    protected List<ValueChangeListener> valueChangeListeners = new ArrayList<>();
    protected Object prevValue;

    private static final int WHOLE_PROGRESS = 100;

    public DesktopProgressBar() {
        impl = new JProgressBar();
        impl.setMinimum(0);
        impl.setMaximum(WHOLE_PROGRESS);
    }

    @Override
    public void addListener(ValueListener listener) {
        addValueChangeListener(new ComponentValueListenerWrapper(listener));
    }

    @Override
    public void removeListener(ValueListener listener) {
        removeValueChangeListener(new ComponentValueListenerWrapper(listener));
    }

    protected void fireValueChanged(Object prevValue, Object value) {
        for (ValueChangeListener listener : new ArrayList<>(valueChangeListeners)) {
            listener.valueChanged(new ValueChangeEvent(this, prevValue, value));
        }
    }

    @Override
    public void addValueChangeListener(ValueChangeListener listener) {
        if (!valueChangeListeners.contains(listener)) {
            valueChangeListeners.add(listener);
        }
    }

    @Override
    public void removeValueChangeListener(ValueChangeListener listener) {
        valueChangeListeners.remove(listener);
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
        DesktopBackgroundWorker.checkSwingUIAccess();

        if (!ObjectUtils.equals(prevValue, value)) {
            updateComponent(value);
            fireChangeListeners(value);
        }
    }

    protected void fireChangeListeners(Object newValue) {
        Object oldValue = prevValue;
        prevValue = newValue;
        if (!ObjectUtils.equals(oldValue, newValue)) {
            fireValueChanged(oldValue, newValue);
        }
    }

    protected void updateComponent(Object value) {
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