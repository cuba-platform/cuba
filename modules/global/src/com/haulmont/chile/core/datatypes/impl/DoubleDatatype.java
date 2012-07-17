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

public class DoubleDatatype extends NumberDatatype implements Datatype<Double> {

	public static String NAME = "double";

    public DoubleDatatype(Element element) {
        super(element);
    }

    public String format(Double value) {
		return value == null ? null : format.format(value);
	}

    @Override
    public String format(Double value, Locale locale) {
        if (value == null)
            return "";

        FormatStrings formatStrings = Datatypes.getFormatStrings(locale);
        if (formatStrings == null)
            return format(value);

        DecimalFormatSymbols formatSymbols = formatStrings.getFormatSymbols();
        NumberFormat format = new DecimalFormat(formatStrings.getDoubleFormat(), formatSymbols);
        return format.format(value);
    }

	public Class getJavaClass() {
		return Double.class;
	}

	public String getName() {
		return NAME;
	}

	public int getSqlType() {
		return Types.NUMERIC;
	}

	public Double parse(String value) throws ParseException {
        if (StringUtils.isBlank(value))
            return null;

        return format.parse(value).doubleValue();
	}

    @Override
    public Double parse(String value, Locale locale) throws ParseException {
        if (StringUtils.isBlank(value))
            return null;

        FormatStrings formatStrings = Datatypes.getFormatStrings(locale);
        if (formatStrings == null)
            return parse(value);

        DecimalFormatSymbols formatSymbols = formatStrings.getFormatSymbols();
        NumberFormat format = new DecimalFormat(formatStrings.getDoubleFormat(), formatSymbols);
        return parse(value, format).doubleValue();
    }

	public Double read(ResultSet resultSet, int index) throws SQLException {
		Double value = resultSet.getDouble(index);
		return resultSet.wasNull() ? null : value;
	}

	public void write(PreparedStatement statement, int index, Double value) throws SQLException {
		if (value == null) {
			statement.setString(index, null);
		} else {
			statement.setDouble(index, value);
		}
	}

    @Override
    public String toString() {
        return NAME;
    }
}