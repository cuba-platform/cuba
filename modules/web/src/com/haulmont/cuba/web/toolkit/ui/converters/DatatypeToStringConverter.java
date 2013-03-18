/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui.converters;

import com.haulmont.chile.core.datatypes.Datatype;
import com.vaadin.data.util.converter.Converter;

import java.text.ParseException;
import java.util.Locale;

/**
 * @author artamonov
 * @version $Id$
 */
public class DatatypeToStringConverter implements Converter<String, Object> {
    private Datatype datatype;

    public DatatypeToStringConverter(Datatype datatype) {
        this.datatype = datatype;
    }

    @Override
    public Object convertToModel(String value, Locale locale) throws ConversionException {
        try {
            if (locale != null)
                return datatype.parse(value, locale);
            else
                return datatype.parse(value);
        } catch (ParseException e) {
            throw new ConversionException(e);
        }
    }

    @Override
    public String convertToPresentation(Object value, Locale locale) throws ConversionException {
        if (locale != null)
            return datatype.format(value, locale);
        else
            return datatype.format(value);
    }

    @Override
    public Class<Object> getModelType() {
        return Object.class;
    }

    @Override
    public Class<String> getPresentationType() {
        return String.class;
    }
}