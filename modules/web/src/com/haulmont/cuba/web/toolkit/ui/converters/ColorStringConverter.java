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

package com.haulmont.cuba.web.toolkit.ui.converters;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.shared.ui.colorpicker.Color;

import java.util.Locale;

public class ColorStringConverter implements Converter<Color, String> {

    @Override
    public String convertToModel(Color value, Class<? extends String> targetType, Locale locale) throws ConversionException {
        if (value == null) {
            return null;
        }

        String redString = Integer.toHexString(value.getRed());
        redString = redString.length() < 2 ? "0" + redString : redString;

        String greenString = Integer.toHexString(value.getGreen());
        greenString = greenString.length() < 2 ? "0" + greenString : greenString;

        String blueString = Integer.toHexString(value.getBlue());
        blueString = blueString.length() < 2 ? "0" + blueString : blueString;

        return redString + greenString + blueString;
    }

    @Override
    public Color convertToPresentation(String value, Class<? extends Color> targetType, Locale locale) throws ConversionException {
        if (value == null) {
            return null;
        }

        if (value.startsWith("#")) {
            value = value.substring(1, value.length());
        }

        try {
            switch (value.length()) {
                case 3:
                    return new Color(Integer.valueOf(value.substring(0, 1), 16),
                            Integer.valueOf(value.substring(1, 2), 16),
                            Integer.valueOf(value.substring(2, 3), 16));
                case 6:
                    return new Color(Integer.valueOf(value.substring(0, 2), 16),
                            Integer.valueOf(value.substring(2, 4), 16),
                            Integer.valueOf(value.substring(4, 6), 16));
                default:
                    throw new ConversionException(String.format("Value '%s' must be 3 or 6 characters in length", value));
            }
        } catch (NumberFormatException e) {
            throw new ConversionException(String.format("Value '%s' is not valid", value));
        }
    }

    @Override
    public Class<String> getModelType() {
        return String.class;
    }

    @Override
    public Class<Color> getPresentationType() {
        return Color.class;
    }
}
