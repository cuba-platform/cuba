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

package com.haulmont.chile.core.datatypes.impl;

import com.haulmont.chile.core.annotations.NumberFormat;
import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.FormatStrings;
import com.haulmont.chile.core.datatypes.FormatStringsRegistry;
import com.haulmont.cuba.core.global.AppBeans;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;

/**
 * A number format that is used when the entity attribute has the {@link NumberFormat} annotation.
 */
public class AdaptiveNumberDatatype extends NumberDatatype implements Datatype<Number> {

    protected Class<?> type;

    public AdaptiveNumberDatatype(Class<?> type, NumberFormat numberFormat) {
        super(numberFormat.pattern(), numberFormat.decimalSeparator(), numberFormat.groupingSeparator());
        this.type = type;
    }

    public AdaptiveNumberDatatype(Class<?> type, String pattern, String decimalSeparator, String groupingSeparator) {
        super(pattern, decimalSeparator, groupingSeparator);
        this.type = type;
    }

    @Override
    protected java.text.NumberFormat createFormat() {
        java.text.NumberFormat numberFormat = super.createFormat();
        setupFormat(numberFormat);
        return numberFormat;
    }

    protected java.text.NumberFormat createLocalizedFormat(Locale locale) {
        FormatStrings formatStrings = AppBeans.get(FormatStringsRegistry.class).getFormatStrings(locale);
        if (formatStrings == null) {
            return createFormat();
        }

        DecimalFormatSymbols formatSymbols = formatStrings.getFormatSymbols();
        if (!decimalSeparator.equals("")) {
            formatSymbols.setDecimalSeparator(decimalSeparator.charAt(0));
        }
        if (!groupingSeparator.equals("")) {
            formatSymbols.setGroupingSeparator(groupingSeparator.charAt(0));
        }

        DecimalFormat format = new DecimalFormat(formatPattern, formatSymbols);
        setupFormat(format);
        return format;
    }

    protected void setupFormat(java.text.NumberFormat numberFormat) {
        if (type.equals(BigDecimal.class) && numberFormat instanceof DecimalFormat)
            ((DecimalFormat) numberFormat).setParseBigDecimal(true);
        if (type.equals(Integer.class) || type.equals(Long.class))
            numberFormat.setParseIntegerOnly(true);
    }

    @Override
    public Class getJavaClass() {
        return type;
    }

    @Override
    public String format(Object value) {
        return value == null ? "" : createFormat().format(value);
    }

    @Override
    public String format(Object value, Locale locale) {
        return value == null ? "" : createLocalizedFormat(locale).format(value);
    }

    @Nullable
    @Override
    public Number parse(@Nullable String value) throws ParseException {
        if (StringUtils.isBlank(value)) {
            return null;
        }

        Number number = parse(value, createFormat());
        checkRange(value, number);
        return requestedType(number);
    }

    @Nullable
    @Override
    public Number parse(@Nullable String value, Locale locale) throws ParseException {
        if (StringUtils.isBlank(value)) {
            return null;
        }

        Number number = parse(value, createLocalizedFormat(locale));
        checkRange(value, number);
        return requestedType(number);
    }

    protected Number requestedType(Number number) {
        if (type.equals(Integer.class))
            return number.intValue();
        if (type.equals(Long.class))
            return number.longValue();
        if (type.equals(Double.class))
            return number.doubleValue();
        if (type.equals(Float.class))
            return number.floatValue();
        return number;
    }

    protected void checkRange(@Nullable String value, Number number) throws ParseException {
        if (type.equals(Integer.class))
            checkIntegerRange(value, number);
        else if (type.equals(Long.class))
            checkLongRange(value, number);
    }

    protected void checkIntegerRange(String value, Number result) throws ParseException {
        if ((result instanceof Long || result instanceof Double)
                && (result.longValue() > Integer.MAX_VALUE || result.longValue() < Integer.MIN_VALUE))
            throw new ParseException(String.format("Integer range exceeded: \"%s\"", value), 0);
    }

    protected void checkLongRange(String value, Number result) throws ParseException {
        if (result instanceof Double
                && (result.doubleValue() > Long.MAX_VALUE || result.doubleValue() < Long.MIN_VALUE))
        throw new ParseException(String.format("Long range exceeded: \"%s\"", value), 0);
    }

    @Override
    public String toString() {
        return "AdaptiveNumberFormat<" + type.getSimpleName() + ">";
    }
}
