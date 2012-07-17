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

import javax.annotation.Nullable;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class DateTimeDatatype implements Datatype<Date> {

	public static String NAME = "dateTime";

    private String formatPattern;

    public DateTimeDatatype(Element element) {
        formatPattern = element.attributeValue("format");
    }

    public String format(Date value) {
        if (value == null)
            return null;
        else {
            DateFormat format;
            if (formatPattern != null) {
                format = new SimpleDateFormat(formatPattern);
            } else {
                format = DateFormat.getDateInstance();
            }
            return format.format((value));
        }
	}

    @Override
    public String format(Date value, Locale locale) {
        if (value == null)
            return "";

        FormatStrings formatStrings = Datatypes.getFormatStrings(locale);
        if (formatStrings == null)
            return format(value);

        DateFormat format = new SimpleDateFormat(formatStrings.getDateTimeFormat());
        return format.format(value);
    }

	public Class getJavaClass() {
		return Date.class;
	}

	public String getName() {
		return NAME;
	}

	public int getSqlType() {
		return Types.TIMESTAMP;
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
        return format.parse(value.trim());
	}

    public Date parse(String value, Locale locale) throws ParseException {
        if (StringUtils.isBlank(value))
            return null;

        FormatStrings formatStrings = Datatypes.getFormatStrings(locale);
        if (formatStrings == null)
            return parse(value);

        DateFormat format = new SimpleDateFormat(formatStrings.getDateTimeFormat());
        return format.parse(value.trim());
    }

	public Date read(ResultSet resultSet, int index) throws SQLException {
		Date value = resultSet.getTimestamp(index);
		return resultSet.wasNull() ? null : value;
	}

	public void write(PreparedStatement statement, int index, Date value) throws SQLException {
		if (value == null) {
			statement.setString(index, null);
		} else {
			statement.setTimestamp(index, new Timestamp(value.getTime()));
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