/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui.converters;

import com.vaadin.data.util.converter.Converter;

import java.util.Locale;

/**
 * @author artamonov
 * @version $Id$
 */
public class SimpleIntegerToStringConverter implements Converter<String, Integer> {

    @Override
    public Integer convertToModel(String value, Class<? extends Integer> targetType, Locale locale)
            throws ConversionException {
        return Integer.parseInt(value);
    }

    @Override
    public String convertToPresentation(Integer value, Class<? extends String> targetType, Locale locale)
            throws ConversionException {
        return String.valueOf(value);
    }

    @Override
    public Class<Integer> getModelType() {
        return Integer.class;
    }

    @Override
    public Class<String> getPresentationType() {
        return String.class;
    }
}