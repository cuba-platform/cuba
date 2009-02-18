/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 29.12.2008 17:16:49
 * $Id$
 */
package com.haulmont.cuba.web.gui.data;

import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.Range;
import com.haulmont.cuba.gui.MetadataHelper;
import com.haulmont.cuba.gui.data.Datasource;
import com.itmill.toolkit.data.Property;

import java.text.ParseException;

public class PropertyWrapper implements Property {
    private boolean readOnly;
    private Object item;
    private MetaProperty metaProperty;

    public PropertyWrapper(Object item, MetaProperty metaProperty) {
        this.item = item;
        this.metaProperty = metaProperty;
    }

    public Object getValue() {
        final Instance instance = getInstance();
        return instance == null ? null : instance.getValue(metaProperty.getName());
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
        
        instance.setValue(metaProperty.getName(), getValue(newValue));
    }

    protected Object getValue(Object newValue) throws Property.ConversionException{
        final Range range = metaProperty.getRange();
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
        return MetadataHelper.getPropertyTypeClass(metaProperty);
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
        return metaProperty.getRange().isDatatype() ?
                metaProperty.getRange().asDatatype().format(value) :
                value == null ? null : value.toString();
    }
}
