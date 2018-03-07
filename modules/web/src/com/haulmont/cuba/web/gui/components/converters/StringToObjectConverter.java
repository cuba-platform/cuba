/*
 * Copyright (c) 2008-2017 Haulmont.
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

import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.MetadataTools;
import com.vaadin.v7.data.util.converter.Converter;

import java.util.Locale;

public class StringToObjectConverter implements Converter<String, Object> {
    protected MetaProperty metaProperty;
    protected MetadataTools metadataTools = AppBeans.get(MetadataTools.NAME);

    public StringToObjectConverter(MetaProperty metaProperty) {
        this.metaProperty = metaProperty;
    }


    @Override
    public Object convertToModel(String value, Class<?> targetType, Locale locale)
            throws ConversionException {
        return null;
    }

    @Override
    public String convertToPresentation(Object value, Class<? extends String> targetType, Locale locale)
            throws ConversionException {
        return metaProperty != null
                ? metadataTools.format(value, metaProperty)
                : metadataTools.format(value);
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