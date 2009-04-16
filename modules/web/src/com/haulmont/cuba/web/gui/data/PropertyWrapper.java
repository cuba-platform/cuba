/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 29.12.2008 17:16:49
 * $Id$
 */
package com.haulmont.cuba.web.gui.data;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.Range;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DatasourceListener;
import com.itmill.toolkit.data.Property;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class PropertyWrapper implements Property, Property.ValueChangeNotifier {
    private boolean readOnly;
    private Object item;

    protected MetaPropertyPath propertyPath;

    private List<ValueChangeListener> listeners = new ArrayList<ValueChangeListener>();

    public PropertyWrapper(Object item, MetaPropertyPath propertyPath) {
        this.item = item;
        this.propertyPath = propertyPath;
        if (item instanceof Datasource) {
            ((Datasource) item).addListener(new DatasourceListener<Entity>() {
                public void itemChanged(Datasource<Entity> ds, Entity prevItem, Entity item) {
                    fireValueChangeEvent();
                }

                public void stateChanged(Datasource<Entity> ds, Datasource.State prevState, Datasource.State state) {}

                public void valueChanged(Entity source, String property, Object prevValue, Object value) {
                    fireValueChangeEvent();
                }
            });
        }
    }

    protected void fireValueChangeEvent() {
        final ValueChangeEvent changeEvent = new ValueChangeEvent();
        for (ValueChangeListener listener : listeners) {
            listener.valueChange(changeEvent);
        }
    }

    public Object getValue() {
        final Instance instance = getInstance();
        return instance == null ? null : InstanceUtils.getValueEx(instance, propertyPath.getPath());
    }

    protected Instance getInstance() {
        if (item instanceof Datasource) {
            final Datasource ds = (Datasource) item;
            if (Datasource.State.VALID.equals(ds.getState())) {
                return (Instance) ds.getItem();
            } else {
                return null;
            }
        } else {
            return (Instance) item;
        }
    }

    public void setValue(Object newValue) throws ReadOnlyException, ConversionException {
        final Instance instance = getInstance();
        if (instance == null) throw new IllegalStateException("Instance is null");
        
        InstanceUtils.setValueEx(instance, propertyPath.getPath(), valueOf(newValue));
    }

    protected Object valueOf(Object newValue) throws Property.ConversionException{
        final Range range = propertyPath.getRange();
        if (range == null) {
            return newValue;
        } else {
            if (range.isDatatype() && newValue instanceof String) {
                try {
                    final Object value = range.asDatatype().parse((String) newValue);
                    return value;
                } catch (ParseException e) {
                    throw new Property.ConversionException(e);
                }
            } else {
                return newValue;
            }
        }
    }

    public Class getType() {
        return propertyPath.getRangeJavaClass();
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean newStatus) {
        readOnly = newStatus;
    }

    @Override
    public String toString() {
        final Object value = getValue();
        final Range range = propertyPath.getRange();
        return range.isDatatype() ?
                range.asDatatype().format(value) :
                value == null ? null : value.toString();
    }

    public void addListener(ValueChangeListener listener) {
        if (!listeners.contains(listener)) listeners.add(listener);
    }

    public void removeListener(ValueChangeListener listener) {
        listeners.remove(listener);
    }

    private class ValueChangeEvent implements Property.ValueChangeEvent {
        public Property getProperty() {
            return PropertyWrapper.this;
        }
    }
}
