/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.data;

import com.google.common.base.Strings;
import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.Range;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.impl.WeakItemChangeListener;
import com.haulmont.cuba.gui.data.impl.WeakItemPropertyChangeListener;
import com.vaadin.data.Property;
import com.vaadin.data.util.converter.Converter;

import java.text.ParseException;

/**
 * @author abramov
 * @version $Id$
 */
public class PropertyWrapper extends AbstractPropertyWrapper implements PropertyValueStringify {

    protected MetaPropertyPath propertyPath;

    protected MetadataTools metadataTools = AppBeans.get(MetadataTools.NAME);

    protected Datasource.ItemChangeListener dsItemChangeListener;
    protected Datasource.ItemPropertyChangeListener dsItemPropertyChangeListener;

    public PropertyWrapper(Object item, MetaPropertyPath propertyPath) {
        this.item = item;
        this.propertyPath = propertyPath;

        if (item instanceof Datasource) {
            dsItemChangeListener = e -> fireValueChangeEvent();
            dsItemPropertyChangeListener = e -> {
                if (e.getProperty().equals(this.propertyPath.toString())) {
                    fireValueChangeEvent();
                }
            };

            Datasource datasource = (Datasource) item;

            //noinspection unchecked
            datasource.addItemChangeListener(new WeakItemChangeListener(datasource, dsItemChangeListener));
            //noinspection unchecked
            datasource.addItemPropertyChangeListener(new WeakItemPropertyChangeListener(datasource, dsItemPropertyChangeListener));
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
    public void setValue(Object newValue) throws Property.ReadOnlyException, Converter.ConversionException {
        final Instance instance = getInstance();

        if (instance != null) {
            InstanceUtils.setValueEx(instance, propertyPath.getPath(), valueOf(newValue));
        }
    }

    protected Object valueOf(Object newValue) throws Converter.ConversionException {
        if (newValue == null) {
            return null;
        }

        final Range range = propertyPath.getRange();
        if (range == null) {
            return newValue;
        } else {
            final Object obj;
            if (range.isDatatype()) {
                Datatype<Object> datatype = range.asDatatype();
                if (newValue instanceof String) {
                    try {
                        newValue = Strings.emptyToNull((String) newValue);
                        UserSessionSource sessionSource = AppBeans.get(UserSessionSource.NAME);
                        obj = datatype.parse((String) newValue, sessionSource.getLocale());
                    } catch (ParseException e) {
                        throw new Converter.ConversionException(e);
                    }
                } else {
                    if (newValue.getClass().equals(datatype.getJavaClass())) {
                        return newValue;
                    } else {
                        Datatype newValueDatatype = Datatypes.getNN(newValue.getClass());
                        //noinspection unchecked
                        String str = newValueDatatype.format(newValue);
                        try {
                            obj = datatype.parse(str);
                        } catch (ParseException e) {
                            throw new Converter.ConversionException(e);
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
    public String getFormattedValue() {
        return metadataTools.format(getValue(), propertyPath.getMetaProperty());
    }
}