/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.data;

import com.vaadin.data.Property;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPropertyWrapper implements Property, Property.ValueChangeNotifier {
    protected boolean readOnly;
    protected Object item;

    protected List<ValueChangeListener> listeners = new ArrayList<ValueChangeListener>();

    public Object getValue() {
        return item;
    }

    public void setValue(Object newValue) throws ReadOnlyException, ConversionException {
        item = newValue;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean newStatus) {
        readOnly = newStatus;
    }

    protected void fireValueChangeEvent() {
        final ValueChangeEvent changeEvent = createValueChangeEvent();
        for (ValueChangeListener listener : new ArrayList<ValueChangeListener>(listeners)) {
            listener.valueChange(changeEvent);
        }
    }

    protected ValueChangeEvent createValueChangeEvent() {
        return new ValueChangeEvent();
    }

    public void addListener(ValueChangeListener listener) {
        if (!listeners.contains(listener)) listeners.add(listener);
    }

    public void removeListener(ValueChangeListener listener) {
        listeners.remove(listener);
    }

    protected class ValueChangeEvent implements Property.ValueChangeEvent {
        public Property getProperty() {
            return AbstractPropertyWrapper.this;
        }
    }
}
