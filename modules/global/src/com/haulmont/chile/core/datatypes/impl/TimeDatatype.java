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
import javax.annotation.Nullable;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * @author devyatkin
 * @version $Id$
 */
public class TimeDatatype implements Datatype<Time> {

    public static String NAME = "time";

    private String formatPattern;

    public TimeDatatype(Element element) {
        formatPattern = element.attributeValue("format");
    }

    @Nonnull
    @Override
    public String format(Time value) {
        if (value == null) {
            return "";
        } else {
            DateFormat format;
            if (formatPattern != null) {
                format = new SimpleDateFormat(formatPattern);
            } else {
                format = DateFormat.getTimeInstance();
            }
            format.setLenient(false);
            return format.format(value);
        }
    }

    @Nonnull
    @Override
    public String format(Time value, Locale locale) {
        if (value == null) {
            return "";
        }

        FormatStrings formatStrings = Datatypes.getFormatStrings(locale);
        if (formatStrings == null) {
            return format(value);
        }

        DateFormat format = new SimpleDateFormat(formatStrings.getTimeFormat());
        format.setLenient(false);

        return format.format(value);
    }

    @Override
    public Class getJavaClass() {
        return Time.class;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public int getSqlType() {
        return Types.TIME;
    }

    @Override
    public Time parse(String value) throws ParseException {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        DateFormat format;
        if (formatPattern != null) {
            format = new SimpleDateFormat(formatPattern);
        } else {
            format = DateFormat.getTimeInstance();
        }

        return new Time(format.parse(value.trim()).getTime());
    }

    @Override
    public Time parse(String value, Locale locale) throws ParseException {
        if (StringUtils.isBlank(value)) {
            return null;
        }

        FormatStrings formatStrings = Datatypes.getFormatStrings(locale);
        if (formatStrings == null) {
            return parse(value);
        }

        DateFormat format = new SimpleDateFormat(formatStrings.getTimeFormat());
        return new Time(format.parse(value.trim()).getTime());
    }

    @Override
    public Time read(ResultSet resultSet, int index) throws SQLException {
        Time value = resultSet.getTime(index);
        return resultSet.wasNull() ? null : value;
    }

    @Override
    public void write(PreparedStatement statement, int index, Time value) throws SQLException {
        if (value == null) {
            statement.setString(index, null);
        } else {
            statement.setTime(index, new java.sql.Time(value.getTime()));
        }
    }

    @Nullable
    public String getFormatPattern() {
        return formatPattern;
    }

    @Override
    public String toString() {
        return NAME;
    }
}