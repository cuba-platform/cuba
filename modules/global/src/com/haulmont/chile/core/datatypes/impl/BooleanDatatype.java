/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.chile.core.datatypes.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.Locale;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.datatypes.FormatStrings;
import org.apache.commons.lang.StringUtils;

public class BooleanDatatype implements Datatype<Boolean> {

	public static String NAME = "boolean";

	public String format(Boolean value) {
		return value == null ? null : Boolean.toString(value);
	}

    @Override
    public String format(Boolean value, Locale locale) {
        if (value == null)
            return "";

        FormatStrings formatStrings = Datatypes.getFormatStrings(locale);
        if (formatStrings == null)
            return format(value);

        return value ? formatStrings.getTrueString() : formatStrings.getFalseString();
    }

	public Class getJavaClass() {
		return Boolean.class;
	}

	public String getName() {
		return NAME;
	}

	public int getSqlType() {
		return Types.BOOLEAN;
	}

    public Boolean parse(String value) throws ParseException {
        if (!StringUtils.isBlank(value)) {
            String lowerCaseValue = StringUtils.lowerCase(value);
            if ("true".equals(lowerCaseValue))
                return true;
            if ("false".equals(lowerCaseValue))
                return false;
        }
        throw new ParseException(String.format("Can't parse '%s", value), 0);
    }

    @Override
    public Boolean parse(String value, Locale locale) throws ParseException {
        if (StringUtils.isBlank(value))
            return null;

        FormatStrings formatStrings = Datatypes.getFormatStrings(locale);
        if (formatStrings == null)
            return parse(value);
        if (value.trim().equalsIgnoreCase(formatStrings.getTrueString()))
            return true;
        if (value.trim().equalsIgnoreCase(formatStrings.getFalseString()))
            return false;
        throw new ParseException(String.format("Can't parse '%s", value), 0);
    }

	public Boolean read(ResultSet resultSet, int index) throws SQLException {
		Boolean value = resultSet.getBoolean(index);
		return resultSet.wasNull() ? null : value;
	}

	public void write(PreparedStatement statement, int index, Boolean value) throws SQLException {
		if (value == null) {
			statement.setString(index, null);
		} else {
			statement.setBoolean(index, value);
		}
	}

    @Override
    public String toString() {
        return NAME;
    }
}