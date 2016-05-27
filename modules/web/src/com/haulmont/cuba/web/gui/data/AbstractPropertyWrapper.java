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
package com.haulmont.cuba.web.gui.data;

import com.vaadin.data.Property;
import com.vaadin.data.util.converter.Converter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractPropertyWrapper implements Property, Property.ValueChangeNotifier {

    protected boolean readOnly;
    protected Object item;

    // lazily initialized listeners list
    protected List<ValueChangeListener> listeners = null;

    @Override
    public Object getValue() {
        return item;
    }

    @Override
    public void setValue(Object newValue) throws ReadOnlyException, Converter.ConversionException {
        item = newValue;
    }

    @Override
    public boolean isReadOnly() {
        return readOnly;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    protected void fireValueChangeEvent() {
        if (listeners != null) {
            final ValueChangeEvent changeEvent = new ValueChangeEvent();
            for (ValueChangeListener listener : new ArrayList<>(listeners)) {
                listener.valueChange(changeEvent);
            }
        }
    }

    @Override
    public void addValueChangeListener(ValueChangeListener listener) {
        if (listeners == null) {
            listeners = new LinkedList<>();
        }

        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    @Override
    public void addListener(ValueChangeListener listener) {
        addValueChangeListener(listener);
    }

    @Override
    public void removeValueChangeListener(ValueChangeListener listener) {
        if (listeners != null) {
            listeners.remove(listener);

            if (listeners.isEmpty()) {
                listeners = null;
            }
        }
    }

    @Override
    public void removeListener(ValueChangeListener listener) {
        removeValueChangeListener(listener);
    }

    protected class ValueChangeEvent implements Property.ValueChangeEvent {
        @Override
        public Property getProperty() {
            return AbstractPropertyWrapper.this;
        }
    }
}