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

package com.haulmont.chile.core.datatypes.impl;

import com.haulmont.chile.core.annotations.JavaClass;
import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.FormatStrings;
import com.haulmont.chile.core.datatypes.FormatStringsRegistry;
import com.haulmont.cuba.core.global.AppBeans;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

@JavaClass(Long.class)
public class LongDatatype extends NumberDatatype implements Datatype<Long> {

    public LongDatatype(Element element) {
        super(element);
    }

    @Override
    public String format(Object value) {
        return value == null ? "" : createFormat().format(value);
    }

    @Override
    public String format(Object value, Locale locale) {
        if (value == null) {
            return "";
        }

        FormatStrings formatStrings = AppBeans.get(FormatStringsRegistry.class).getFormatStrings(locale);
        if (formatStrings == null) {
            return format(value);
        }

        DecimalFormatSymbols formatSymbols = formatStrings.getFormatSymbols();
        NumberFormat format = new DecimalFormat(formatStrings.getIntegerFormat(), formatSymbols);
        return format.format(value);
    }

    @Override
    public Long parse(String value) throws ParseException {
        if (StringUtils.isBlank(value)) {
            return null;
        }

        return parse(value, createFormat()).longValue();
    }

    @Override
    public Long parse(String value, Locale locale) throws ParseException {
        if (StringUtils.isBlank(value)) {
            return null;
        }

        FormatStrings formatStrings = AppBeans.get(FormatStringsRegistry.class).getFormatStrings(locale);
        if (formatStrings == null) {
            return parse(value);
        }

        DecimalFormatSymbols formatSymbols = formatStrings.getFormatSymbols();
        NumberFormat format = new DecimalFormat(formatStrings.getIntegerFormat(), formatSymbols);

        return parse(value, format).longValue();
    }

    @Override
    protected Number parse(String value, NumberFormat format) throws ParseException {
        format.setParseIntegerOnly(true);

        Number result = super.parse(value, format);
        if (!hasValidLongRange(result)) {
            throw new ParseException(String.format("Integer range exceeded: \"%s\"", value), 0);
        }
        return result;
    }

    protected boolean hasValidLongRange(Number result) throws ParseException {
        if (result instanceof Double) {
            Double doubleResult = (Double) result;
            if (doubleResult > Long.MAX_VALUE || doubleResult < Long.MIN_VALUE) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    @Deprecated
    public final static String NAME = "long";
}