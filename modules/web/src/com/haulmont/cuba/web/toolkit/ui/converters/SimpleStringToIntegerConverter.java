/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.converters;

import com.vaadin.data.util.converter.Converter;
import org.apache.commons.lang.StringUtils;

import java.util.Locale;

/**
 * @author artamonov
 * @version $Id$
 */
public class SimpleStringToIntegerConverter implements Converter<String, Integer> {

    @Override
    public Integer convertToModel(String value, Class<? extends Integer> targetType, Locale locale)
            throws ConversionException {
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new ConversionException(e);
        }
    }

    @Override
    public String convertToPresentation(Integer value, Class<? extends String> targetType, Locale locale)
            throws ConversionException {
        if (value == null) {
            return "";
        }

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