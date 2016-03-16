/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.web.toolkit.ui.converters;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.Formatter;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.server.VaadinSession;
import org.apache.commons.lang.StringUtils;

import java.util.Locale;

/**
 */
public class StringToEnumConverter implements Converter<String, Enum> {

    protected Class<Enum> enumClass;
    protected Messages messages;
    protected Formatter formatter;
    protected boolean trimming = false;

    public StringToEnumConverter(Class<Enum> enumClass) {
        this.enumClass = enumClass;
        this.messages = AppBeans.get(Messages.NAME);
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

        if (isTrimming()) {
            value = StringUtils.trimToEmpty(value);
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

        if (getFormatter() != null) {
            return getFormatter().format(value);
        }

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