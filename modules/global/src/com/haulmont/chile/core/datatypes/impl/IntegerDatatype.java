/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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
 * @author krivopustov
 * @version $Id$
 */
public class IntegerDatatype extends NumberDatatype implements Datatype<Integer> {

    public final static String NAME = "int";

    public IntegerDatatype(Element element) {
        super(element);
    }

    @Nonnull
    @Override
    public String format(Integer value) {
        return value == null ? "" : createFormat().format(value);
    }

    @Nonnull
    @Override
    public String format(Integer value, Locale locale) {
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
    public void write(PreparedStatement statement, int index, Integer value) throws SQLException {
        if (value == null) {
            statement.setString(index, null);
        } else {
            statement.setInt(index, value);
        }
    }

    @Override
    public String toString() {
        return NAME;
    }
}