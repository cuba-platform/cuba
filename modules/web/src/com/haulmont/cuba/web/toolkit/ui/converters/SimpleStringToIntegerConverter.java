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

import com.vaadin.data.util.converter.Converter;
import org.apache.commons.lang.StringUtils;

import java.util.Locale;

/**
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