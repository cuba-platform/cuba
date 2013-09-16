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
 * @author krivopustov
 * @version $Id: BigDecimalDatatype.java 8154 2012-06-14 12:41:12Z artamonov $
 */
public class BigDecimalDatatype extends NumberDatatype implements Datatype<BigDecimal> {

	public static String NAME = "decimal";

    public BigDecimalDatatype(Element element) {
        super(element);
        if (format instanceof DecimalFormat)
            ((DecimalFormat) format).setParseBigDecimal(true);
    }

    @Nonnull
    @Override
    public String format(BigDecimal value) {
		return value == null ? "" : format.format(value);
	}

    @Nonnull
    @Override
    public String format(BigDecimal value, Locale locale) {
        if (value == null)
            return "";

        FormatStrings formatStrings = Datatypes.getFormatStrings(locale);
        if (formatStrings == null)
            return format(value);

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
        if (StringUtils.isBlank(value))
            return null;

        return (BigDecimal) parse(value, format);
	}

    @Override
    public BigDecimal parse(String value, Locale locale) throws ParseException {
        if (StringUtils.isBlank(value))
            return null;

        FormatStrings formatStrings = Datatypes.getFormatStrings(locale);
        if (formatStrings == null)
            return parse(value);

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
	public void write(PreparedStatement statement, int index, BigDecimal value) throws SQLException {
		if (value == null) {
			statement.setString(index, null);
		} else {
			statement.setBigDecimal(index, value);
		}
	}

    @Override
    public String toString() {
        return NAME;
    }
}