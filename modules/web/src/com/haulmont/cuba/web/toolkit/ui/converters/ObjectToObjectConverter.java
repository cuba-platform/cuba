/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.converters;

import com.vaadin.data.util.converter.Converter;

import java.util.Locale;

/**
 * @author devyatkin
 * @version $Id$
 */
public class ObjectToObjectConverter implements Converter<Object, Object> {

    @Override
    public Object convertToModel(Object value, Class<?> targetType, Locale locale) throws ConversionException {
        return value;
    }

    @Override
    public Object convertToPresentation(Object value, Class<?> targetType, Locale locale) throws ConversionException {
        return value;
    }

    @Override
    public Class<Object> getModelType() {
        return Object.class;
    }

    @Override
    public Class<Object> getPresentationType() {
        return Object.class;
    }
}