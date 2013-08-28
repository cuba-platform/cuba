/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui.converters;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.server.VaadinSession;
import org.apache.commons.lang.StringUtils;

import java.util.Locale;

/**
 * @author artamonov
 * @version $Id$
 */
public class StringToEnumConverter implements Converter<String, Enum> {

    protected Class<Enum> enumClass;
    protected Messages messages;

    public StringToEnumConverter(Class<Enum> enumClass) {
        this.enumClass = enumClass;
        this.messages = AppBeans.get(Messages.class);
    }

    @Override
    public Enum convertToModel(String value, Class<? extends Enum> targetType, Locale locale)
            throws ConversionException {

        if (value == null) {
            return null;
        }

        if (locale == null) {
            locale = VaadinSession.getCurrent().getLocale();
        }

        Object[] enumConstants = enumClass.getEnumConstants();
        if (enumConstants != null) {
            for (Object enumValue : enumConstants) {
                if (StringUtils.equals(value, messages.getMessage((Enum) enumValue, locale))) {
                    return (Enum) enumValue;
                }
            }
        }

        return null;
    }

    @Override
    public String convertToPresentation(Enum value, Class<? extends String> targetType, Locale locale)
            throws ConversionException {

        if (value == null) {
            return null;
        }

        if (locale == null) {
            locale = VaadinSession.getCurrent().getLocale();
        }

        return messages.getMessage(value, locale);
    }

    @Override
    public Class<Enum> getModelType() {
        return enumClass;
    }

    @Override
    public Class<String> getPresentationType() {
        return String.class;
    }
}