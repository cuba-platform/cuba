/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
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
    public Object convertToModel(Object value, Locale locale) throws ConversionException {
        return value;
    }

    @Override
    public Object convertToPresentation(Object value, Locale locale) throws ConversionException {
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
