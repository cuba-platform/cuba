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

import com.vaadin.v7.data.util.converter.StringToBooleanConverter;
import org.apache.commons.lang.BooleanUtils;

import java.util.Locale;

public class YesNoIconConverter extends StringToBooleanConverter {

    protected static final String BASE_STYLE = "boolean-value";

    public YesNoIconConverter() {
        super(Boolean.TRUE.toString(), Boolean.FALSE.toString());
    }

    @Override
    public String convertToPresentation(Boolean value, Class<? extends String> targetType, Locale locale)
            throws ConversionException {
        if (BooleanUtils.isTrue(value)) {
            return getHtmlString(getTrueString());
        } else {
            return getHtmlString(getFalseString());
        }
    }

    protected String getHtmlString(String value) {
        return "<div class=\"" + BASE_STYLE + " " + BASE_STYLE + "-" + value + "\"/>";
    }
}