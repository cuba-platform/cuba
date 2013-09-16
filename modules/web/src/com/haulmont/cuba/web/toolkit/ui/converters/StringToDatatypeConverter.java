/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.converters;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.cuba.gui.components.Formatter;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.server.VaadinSession;
import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.util.Locale;

/**
 * @author artamonov
 * @version $Id$
 */
public class StringToDatatypeConverter implements Converter<String, Object> {

    protected Datatype datatype;
    protected Formatter formatter;
    protected boolean trimming = false;

    public StringToDatatypeConverter(Datatype datatype) {
        this.datatype = datatype;
    }

    @Override
    public Object convertToModel(String value, Class<?> targetType, Locale locale) throws ConversionException {
        try {
            if (locale == null) {
                locale = VaadinSession.getCurrent().getLocale();
            }

            if (isTrimming()) {
                value = StringUtils.trimToEmpty(value);
            }

            if (locale != null) {
                return datatype.parse(value, locale);
            }

            return datatype.parse(value);
        } catch (ParseException e) {
            throw new ConversionException(e);
        }
    }

    @Override
    public String convertToPresentation(Object value, Class<? extends String> targetType, Locale locale)
            throws ConversionException {
        if (getFormatter() != null) {
            return getFormatter().format(value);
        }

        if (locale == null) {
            locale = VaadinSession.getCurrent().getLocale();
        }

        if (locale != null) {
            return datatype.format(value, locale);
        }

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

    public Formatter getFormatter() {
        return formatter;
    }

    public void setFormatter(Formatter formatter) {
        this.formatter = formatter;
    }

    public boolean isTrimming() {
        return trimming;
    }

    public void setTrimming(boolean trimming) {
        this.trimming = trimming;
    }
}