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
import java.util.Date;
import java.util.Locale;

/**
 * <code>TimeDatatype</code> works with <code>java.sql.Time</code> but is parameterized with <code>java.util.Date</code>
 * to avoid problems with casting, e.g. <code>org.apache.openjpa.util.java$util$Date$proxy</code>.
 *
 * @author devyatkin
 * @version $Id$
 */
public class TimeDatatype implements Datatype<Date> {

    public final static String NAME = "time";

    private String formatPattern;

    public TimeDatatype(Element element) {
        formatPattern = element.attributeValue("format");
    }

    @Nonnull
    @Override
    public String format(Date value) {
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
    public String format(Date value, Locale locale) {
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
    public Date parse(String value) throws ParseException {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        DateFormat format;
        if (formatPattern != null) {
            format = new SimpleDateFormat(formatPattern);
        } else {
            format = DateFormat.getTimeInstance();
        }

        return format.parse(value.trim());
    }

    @Override
    public Date parse(String value, Locale locale) throws ParseException {
        if (StringUtils.isBlank(value)) {
            return null;
        }

        FormatStrings formatStrings = Datatypes.getFormatStrings(locale);
        if (formatStrings == null) {
            return parse(value);
        }

        DateFormat format = new SimpleDateFormat(formatStrings.getTimeFormat());
        return format.parse(value.trim());
    }

    @Override
    public Date read(ResultSet resultSet, int index) throws SQLException {
        Date value = resultSet.getTime(index);
        return resultSet.wasNull() ? null : value;
    }

    @Override
    public void write(PreparedStatement statement, int index, Date value) throws SQLException {
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