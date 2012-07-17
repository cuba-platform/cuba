/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.chile.core.datatypes.impl;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.datatypes.FormatStrings;
import org.dom4j.Element;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nullable;

public class DateDatatype implements Datatype<Date> {

	public static String NAME = "date";

    private String formatPattern;

    public DateDatatype(Element element) {
        formatPattern = element.attributeValue("format");
    }

    public String format(Date value) {
        if (value == null)
            return null;

        DateFormat format;
        if (formatPattern != null) {
            format = new SimpleDateFormat(formatPattern);
        } else {
            format = DateFormat.getDateInstance();
        }
        return format.format((value));
	}

    public String format(Date value, Locale locale) {
        if (value == null)
            return "";

        FormatStrings formatStrings = Datatypes.getFormatStrings(locale);
        if (formatStrings == null)
            return format(value);

        DateFormat format = new SimpleDateFormat(formatStrings.getDateFormat());
        return format.format(value);
    }

	public Class getJavaClass() {
		return java.sql.Date.class;
	}

	public String getName() {
		return NAME;
	}

	public int getSqlType() {
		return Types.DATE;
	}

    private java.sql.Date normalize(java.util.Date dateTime) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateTime);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return new java.sql.Date(cal.getTimeInMillis());

    }

	public Date parse(String value) throws ParseException {
        if (StringUtils.isBlank(value))
            return null;

        DateFormat format;
        if (formatPattern != null) {
            format = new SimpleDateFormat(formatPattern);
        } else {
            format = DateFormat.getDateInstance();
        }
        return normalize(format.parse(value.trim()));
	}

    public Date parse(String value, Locale locale) throws ParseException {
        if (StringUtils.isBlank(value))
            return null;

        FormatStrings formatStrings = Datatypes.getFormatStrings(locale);
        if (formatStrings == null)
            return parse(value);

        DateFormat format = new SimpleDateFormat(formatStrings.getDateFormat());
        return normalize(format.parse(value.trim()));
    }

	public Date read(ResultSet resultSet, int index) throws SQLException {
		java.sql.Date value = resultSet.getDate(index);
		return resultSet.wasNull() ? null : value;
	}

	public void write(PreparedStatement statement, int index, Date value) throws SQLException {
		if (value == null) {
			statement.setString(index, null);
		} else {
			statement.setDate(index, new java.sql.Date(value.getTime()));
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