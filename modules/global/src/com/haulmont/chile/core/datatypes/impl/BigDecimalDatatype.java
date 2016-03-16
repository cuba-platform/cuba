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
import java.math.BigDecimal;
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
 * @version $Id: BigDecimalDatatype.java 8154 2012-06-14 12:41:12Z artamonov $
 */
public class BigDecimalDatatype extends NumberDatatype implements Datatype<BigDecimal> {

    public final static String NAME = "decimal";

    public BigDecimalDatatype(Element element) {
        super(element);
    }

    @Override
    protected NumberFormat createFormat() {
        NumberFormat format = super.createFormat();
        if (format instanceof DecimalFormat) {
            ((DecimalFormat) format).setParseBigDecimal(true);
        }
        return format;
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
        NumberFormat format = new DecimalFormat(formatStrings.getDecimalFormat(), formatSymbols);
        return format.format(value);
    }

    @Override
    public Class getJavaClass() {
        return BigDecimal.class;
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
    public BigDecimal parse(String value) throws ParseException {
        if (StringUtils.isBlank(value)) {
            return null;
        }

        return (BigDecimal) parse(value, createFormat());
    }

    @Override
    public BigDecimal parse(String value, Locale locale) throws ParseException {
        if (StringUtils.isBlank(value)) {
            return null;
        }

        FormatStrings formatStrings = Datatypes.getFormatStrings(locale);
        if (formatStrings == null) {
            return parse(value);
        }

        DecimalFormatSymbols formatSymbols = formatStrings.getFormatSymbols();
        DecimalFormat format = new DecimalFormat(formatStrings.getDecimalFormat(), formatSymbols);
        format.setParseBigDecimal(true);
        return (BigDecimal) parse(value, format);
    }

    @Override
    public BigDecimal read(ResultSet resultSet, int index) throws SQLException {
        BigDecimal value = resultSet.getBigDecimal(index);
        return resultSet.wasNull() ? null : value;
    }

    @Override
    public void write(PreparedStatement statement, int index, Object value) throws SQLException {
        if (value == null) {
            statement.setString(index, null);
        } else {
            statement.setBigDecimal(index, (BigDecimal) value);
        }
    }

    @Override
    public String toString() {
        return NAME;
    }
}