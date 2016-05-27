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

import java.util.Locale;

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