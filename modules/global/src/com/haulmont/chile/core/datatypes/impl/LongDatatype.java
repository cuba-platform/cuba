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
import java.text.*;
import java.util.Locale;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.datatypes.FormatStrings;
import org.dom4j.Element;
import org.apache.commons.lang.StringUtils;

public class LongDatatype extends NumberDatatype implements Datatype<Long> {

	public static String NAME = "long";

    public LongDatatype(Element element) {
        super(element);
    }

    public String format(Long value) {
        return value == null ? null : format.format(value);
	}

    @Override
    public String format(Long value, Locale locale) {
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
		return Long.class;
	}

	public String getName() {
		return NAME;
	}

	public int getSqlType() {
		return Types.BIGINT;
	}

	public Long parse(String value) throws ParseException {
        if (StringUtils.isBlank(value))
            return null;

        return parse(value, format).longValue();
	}

    @Override
    public Long parse(String value, Locale locale) throws ParseException {
        if (StringUtils.isBlank(value))
            return null;

        FormatStrings formatStrings = Datatypes.getFormatStrings(locale);
        if (formatStrings == null)
            return parse(value);

        DecimalFormatSymbols formatSymbols = formatStrings.getFormatSymbols();
        NumberFormat format = new DecimalFormat(formatStrings.getIntegerFormat(), formatSymbols);
        return parse(value, format).longValue();
    }

	public Long read(ResultSet resultSet, int index) throws SQLException {
		Long value = resultSet.getLong(index);
		return resultSet.wasNull() ? null : value;
	}

	public void write(PreparedStatement statement, int index, Long value) throws SQLException {
		if (value == null) {
			statement.setString(index, null);
		} else {
			statement.setLong(index, value);
		}
	}

    @Override
    public String toString() {
        return NAME;
    }
}