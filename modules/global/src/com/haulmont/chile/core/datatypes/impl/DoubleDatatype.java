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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.*;
import java.util.Locale;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.datatypes.FormatStrings;
import org.dom4j.Element;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nonnull;

/**
 */
public class DoubleDatatype extends NumberDatatype implements Datatype<Double> {

    public final static String NAME = "double";

    public DoubleDatatype(Element element) {
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
        if (value == null) {
            return "";
        }

        FormatStrings formatStrings = Datatypes.getFormatStrings(locale);
        if (formatStrings == null) {
            return format(value);
        }

        DecimalFormatSymbols formatSymbols = formatStrings.getFormatSymbols();
        NumberFormat format = new DecimalFormat(formatStrings.getDoubleFormat(), formatSymbols);
        return format.format(value);
    }

    @Override
    public Class getJavaClass() {
        return Double.class;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public int getSqlType() {
        return Types.NUMERIC;
    }

    @Override
    public Double parse(String value) throws ParseException {
        if (StringUtils.isBlank(value)) {
            return null;
        }

        return parse(value, createFormat()).doubleValue();
    }

    @Override
    public Double parse(String value, Locale locale) throws ParseException {
        if (StringUtils.isBlank(value)) {
            return null;
        }

        FormatStrings formatStrings = Datatypes.getFormatStrings(locale);
        if (formatStrings == null) {
            return parse(value);
        }

        DecimalFormatSymbols formatSymbols = formatStrings.getFormatSymbols();
        NumberFormat format = new DecimalFormat(formatStrings.getDoubleFormat(), formatSymbols);
        return parse(value, format).doubleValue();
    }

    @Override
    public Double read(ResultSet resultSet, int index) throws SQLException {
        Double value = resultSet.getDouble(index);
        return resultSet.wasNull() ? null : value;
    }

    @Override
    public void write(PreparedStatement statement, int index, Object value) throws SQLException {
        if (value == null) {
            statement.setString(index, null);
        } else {
            statement.setDouble(index, (Double) value);
        }
    }

    @Override
    public String toString() {
        return NAME;
    }
}