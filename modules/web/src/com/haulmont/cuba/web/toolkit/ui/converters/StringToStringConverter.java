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
public class StringToStringConverter implements Converter<String, String> {
    @Override
    public String convertToModel(String value, Locale locale) throws ConversionException {
        return value;
    }

    @Override
    public String convertToPresentation(String value, Locale locale) throws ConversionException {
        return value;
    }

    @Override
    public Class<String> getModelType() {
        return String.class;
    }

    @Override
    public Class<String> getPresentationType() {
        return String.class;
    }
}