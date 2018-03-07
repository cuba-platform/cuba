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
 */

package com.haulmont.cuba.web.gui.components.converters;

import com.vaadin.v7.data.util.converter.Converter;

import java.util.*;

public class SetToListConverter implements Converter<Object, List> {
    @Override
    public List convertToModel(Object value, Class<? extends List> targetType, Locale locale) throws ConversionException {
        if (value == null)
            return null;

        return new ArrayList((Collection) value);
    }

    @Override
    public Object convertToPresentation(List value, Class<?> targetType, Locale locale) throws ConversionException {
        if (value == null)
            return null;

        return new LinkedHashSet<>(value);
    }

    @Override
    public Class<List> getModelType() {
        return List.class;
    }

    @Override
    public Class<Object> getPresentationType() {
        return Object.class;
    }
}
