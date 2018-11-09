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

package com.haulmont.cuba.web.gui.components.converters;

import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.entity.Entity;
import com.vaadin.v7.data.util.converter.Converter;

import java.util.Locale;
import java.util.function.Function;

/**
 * todo replace with direct MetadataTools call
 */
@Deprecated
public class StringToEntityConverter implements Converter<String, Entity> {

    protected Function<Object, String> formatter;

    @Override
    public Entity convertToModel(String value, Class<? extends Entity> targetType, Locale locale)
            throws ConversionException {
        return null;
    }

    @Override
    public String convertToPresentation(Entity value, Class<? extends String> targetType, Locale locale)
            throws ConversionException {
        if (getFormatter() != null) {
            return getFormatter().apply(value);
        }

        if (value != null) {
            return InstanceUtils.getInstanceName(value);
        }

        return "";
    }

    @Override
    public Class<Entity> getModelType() {
        return Entity.class;
    }

    public Function<Object, String> getFormatter() {
        return formatter;
    }

    public void setFormatter(Function<Object, String> formatter) {
        this.formatter = formatter;
    }

    @Override
    public Class<String> getPresentationType() {
        return String.class;
    }
}