/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.data;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.Range;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.core.global.UserSessionProvider;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DatasourceListener;
import com.vaadin.data.Property;

import java.text.ParseException;

/**
 * @author abramov
 * @version $Id$
 */
public class PropertyWrapper extends AbstractPropertyWrapper {

    private static final long serialVersionUID = 5863216328152195113L;

    protected MetaPropertyPath propertyPath;

    protected MetadataTools metadataTools = AppBeans.get(MetadataTools.NAME);

    public PropertyWrapper(Object item, MetaPropertyPath propertyPath) {
        this.item = item;
        this.propertyPath = propertyPath;
        if (item instanceof Datasource) {
            ((Datasource) item).addListener(new DatasourceListener<Entity>() {
                @Override
                public void itemChanged(Datasource<Entity> ds, Entity prevItem, Entity item) {
                    fireValueChangeEvent();
                }

                @Override
                public void stateChanged(Datasource<Entity> ds, Datasource.State prevState, Datasource.State state) {
                }

                @Override
                public void valueChanged(Entity source, String property, Object prevValue, Object value) {
                    if (property.equals(PropertyWrapper.this.propertyPath.toString()))
                        fireValueChangeEvent();
                }
            });
        }
    }

    @Override
    public Object getValue() {
        final Instance instance = getInstance();
        return instance == null ? null : InstanceUtils.getValueEx(instance, propertyPath.getPath());
    }

    protected Instance getInstance() {
        if (item instanceof Datasource) {
            final Datasource ds = (Datasource) item;
            if (Datasource.State.VALID.equals(ds.getState())) {
                return ds.getItem();
            } else {
                return null;
            }
        } else {
            return (Instance) item;
        }
    }

    @Override
    public void setValue(Object newValue) throws ReadOnlyException, ConversionException {
        final Instance instance = getInstance();

        if (instance != null)
            InstanceUtils.setValueEx(instance, propertyPath.getPath(), valueOf(newValue));
    }

    protected Object valueOf(Object newValue) throws Property.ConversionException {
        if (newValue == null)
            return newValue;
        final Range range = propertyPath.getRange();
        if (range == null) {
            return newValue;
        } else {
            final Object obj;
            if (range.isDatatype()) {
                Datatype<Object> datatype = range.asDatatype();
                if (newValue instanceof String) {
                    try {
                        obj = datatype.parse((String) newValue, UserSessionProvider.getLocale());
                    } catch (ParseException e) {
                        throw new Property.ConversionException(e);
                    }
                } else {
                    if (newValue.getClass().equals(datatype.getJavaClass())) {
                        return newValue;
                    } else {
                        Datatype newValueDatatype = Datatypes.getNN(newValue.getClass());
                        String str = newValueDatatype.format(newValue);
                        try {
                            obj = datatype.parse(str);
                        } catch (ParseException e) {
                            throw new Property.ConversionException(e);
                        }
                    }
                }
            } else {
                obj = newValue;
            }
            return obj;
        }
    }

    @Override
    public Class getType() {
        return propertyPath.getRangeJavaClass();
    }

    @Override
    public String toString() {
        Object value = getValue();
        return metadataTools.format(value, propertyPath.getMetaProperty());
    }
}
