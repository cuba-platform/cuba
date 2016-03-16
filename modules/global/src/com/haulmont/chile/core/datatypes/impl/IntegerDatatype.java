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

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.datatypes.FormatStrings;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import javax.annotation.Nonnull;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

/**
 */
public class IntegerDatatype extends NumberDatatype implements Datatype<Integer> {

    public final static String NAME = "int";

    public IntegerDatatype(Element element) {
        super(element);
    }

    @Nonnull
    @Override
    public String format(Object value) {
        return value == null ? "" : createFormat().format(value);
    }

    @Nonnull
    @Override
    public String format(Object value, Locale locale) {
        if (value == null)
            return "";

        FormatStrings formatStrings = Datatypes.getFormatStrings(locale);
        if (formatStrings == null)
            return format(value);

        DecimalFormatSymbols formatSymbols = formatStrings.getFormatSymbols();
        NumberFormat format = new DecimalFormat(formatStrings.getIntegerFormat(), formatSymbols);
        return format.format(value);
    }

    @Override
    public Class getJavaClass() {
        return Integer.class;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public int getSqlType() {
        return Types.INTEGER;
    }

    @Override
    public Integer parse(String value) throws ParseException {
        if (StringUtils.isBlank(value))
            return null;

        return parse(value, createFormat()).intValue();
    }

    @Override
    public Integer parse(String value, Locale locale) throws ParseException {
        if (StringUtils.isBlank(value))
            return null;

        FormatStrings formatStrings = Datatypes.getFormatStrings(locale);
        if (formatStrings == null)
            return parse(value);

        DecimalFormatSymbols formatSymbols = formatStrings.getFormatSymbols();
        NumberFormat format = new DecimalFormat(formatStrings.getIntegerFormat(), formatSymbols);
        return parse(value, format).intValue();
    }

    @Override
    protected Number parse(String value, NumberFormat format) throws ParseException {
        format.setParseIntegerOnly(true);

        Number result = super.parse(value, format);
        if (!hasValidIntegerRange(result)) {
            throw new ParseException(String.format("Integer range exceeded: \"%s\"", value), 0);
        }
        return result;
    }

    protected boolean hasValidIntegerRange(Number result) throws ParseException {
        if (result instanceof Long) {
            Long longResult = (Long) result;

            if (longResult > Integer.MAX_VALUE || longResult < Integer.MIN_VALUE) {
                return false;
            }
        } else {
            Double doubleResult = (Double) result;
            if (doubleResult > Integer.MAX_VALUE || doubleResult < Integer.MIN_VALUE) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Integer read(ResultSet resultSet, int index) throws SQLException {
        Integer value = resultSet.getInt(index);
        return resultSet.wasNull() ? null : value;
    }

    @Override
    public void write(PreparedStatement statement, int index, Object value) throws SQLException {
        if (value == null) {
            statement.setString(index, null);
        } else {
            statement.setInt(index, (Integer) value);
        }
    }

    @Override
    public String toString() {
        return NAME;
    }
}