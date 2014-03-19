/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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

import javax.annotation.Nonnull;

/**
 * @author krivopustov
 * @version $Id$
 */
public class DoubleDatatype extends NumberDatatype implements Datatype<Double> {

    public final static String NAME = "double";

    public DoubleDatatype(Element element) {
        super(element);
    }

    @Nonnull
    @Override
    public String format(Double value) {
        return value == null ? "" : format.format(value);
    }

    @Nonnull
    @Override
    public String format(Double value, Locale locale) {
        if (value == null) {
            return "";
        }

        FormatStrings formatStrings = Datatypes.getFormatStrings(locale);
        if (formatStrings == null) {
            return format(value);
        }

        DecimalFormatSymbols formatSymbols = formatStrings.getFormatSymbols();
        NumberFormat format = new DecimalFormat(formatStrings.getDoubleFormat(), formatSymbols);
        return format.format(value);
    }

    @Override
    public Class getJavaClass() {
        return Double.class;
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
    public Double parse(String value) throws ParseException {
        if (StringUtils.isBlank(value)) {
            return null;
        }

        return parse(value, format).doubleValue();
    }

    @Override
    public Double parse(String value, Locale locale) throws ParseException {
        if (StringUtils.isBlank(value)) {
            return null;
        }

        FormatStrings formatStrings = Datatypes.getFormatStrings(locale);
        if (formatStrings == null) {
            return parse(value);
        }

        DecimalFormatSymbols formatSymbols = formatStrings.getFormatSymbols();
        NumberFormat format = new DecimalFormat(formatStrings.getDoubleFormat(), formatSymbols);
        return parse(value, format).doubleValue();
    }

    @Override
    public Double read(ResultSet resultSet, int index) throws SQLException {
        Double value = resultSet.getDouble(index);
        return resultSet.wasNull() ? null : value;
    }

    @Override
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