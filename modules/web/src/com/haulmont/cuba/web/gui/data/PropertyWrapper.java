/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 29.12.2008 17:16:49
 * $Id$
 */
package com.haulmont.cuba.web.gui.data;

import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.Range;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.core.global.MessageUtils;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DatasourceListener;
import com.vaadin.data.Property;

import javax.persistence.TemporalType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PropertyWrapper extends AbstractPropertyWrapper {
    protected MetaPropertyPath propertyPath;

    private static final long serialVersionUID = 5863216328152195113L;

    public PropertyWrapper(Object item, MetaPropertyPath propertyPath, DsManager dsManager) {
        this.item = item;
        this.propertyPath = propertyPath;
        if (item instanceof Datasource) {
            dsManager.addListener(new DatasourceListener<Entity>() {
                public void itemChanged(Datasource<Entity> ds, Entity prevItem, Entity item) {
                    fireValueChangeEvent();
                }

                public void stateChanged(Datasource<Entity> ds, Datasource.State prevState, Datasource.State state) {
                }

                public void valueChanged(Entity source, String property, Object prevValue, Object value) {
                    if (property.equals(PropertyWrapper.this.propertyPath.toString()))
                        fireValueChangeEvent();
                }
            });
        }
    }

    public Object getValue() {
        final Instance instance = getInstance();
        Object value = instance == null ? null : InstanceUtils.getValueEx(instance, propertyPath.getPath());
        if (value == null && propertyPath.getRange().isDatatype()
                && propertyPath.getRange().asDatatype().equals(Datatypes.getInstance().get(Boolean.class))) {
            value = Boolean.FALSE;
        }
        return value;
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

    protected Object valueOf(Object newValue) throws Property.ConversionException {
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

    @Override
    public String toString() {
        final Object value = getValue();
        if (value == null) return null;

        final Range range = propertyPath.getRange();
        if (range.isDatatype()) {
            if (range.asDatatype().equals(Datatypes.getInstance().get(Date.class))) {
                String formatStr;
                TemporalType tt = (TemporalType) propertyPath.getMetaProperty().getAnnotations().get("temporal");
                if (TemporalType.DATE.equals(tt)) {
                    formatStr = MessageUtils.getDateFormat();
                } else {
                    formatStr = MessageUtils.getDateTimeFormat();
                }
                return new SimpleDateFormat(formatStr).format(value);
            } else {
                return range.asDatatype().format(value);
            }
        } else if (range.isEnum()) {
            String nameKey = value.getClass().getSimpleName() + "." + value.toString();
            return MessageProvider.getMessage(value.getClass(), nameKey);
        } else {
            if (value instanceof Instance)
                return ((Instance) value).getInstanceName();
            else
                return value.toString();
        }
    }
}
