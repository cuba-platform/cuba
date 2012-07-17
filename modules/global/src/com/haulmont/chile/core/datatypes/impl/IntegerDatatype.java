/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.chile.core.datatypes.impl;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.datatypes.FormatStrings;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class IntegerDatatype extends NumberDatatype implements Datatype<Integer> {

    public static String NAME = "int";

    public IntegerDatatype(Element element) {
        super(element);
    }

    public String format(Integer value) {
        return value == null ? null : format.format(value);
    }

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

    public Class getJavaClass() {
        return Integer.class;
    }

    public String getName() {
        return NAME;
    }

    public int getSqlType() {
        return Types.INTEGER;
    }

    public Integer parse(String value) throws ParseException {
        if (StringUtils.isBlank(value))
            return null;

        return parse(value, format).intValue();
    }

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

    public Integer read(ResultSet resultSet, int index) throws SQLException {
        Integer value = resultSet.getInt(index);
        return resultSet.wasNull() ? null : value;
    }

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
