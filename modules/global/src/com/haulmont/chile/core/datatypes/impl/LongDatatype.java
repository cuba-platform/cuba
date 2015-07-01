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
public class LongDatatype extends NumberDatatype implements Datatype<Long> {

    public final static String NAME = "long";

    public LongDatatype(Element element) {
        super(element);
    }

    @Nonnull
    @Override
    public String format(Long value) {
        return value == null ? "" : createFormat().format(value);
    }

    @Nonnull
    @Override
    public String format(Long value, Locale locale) {
        if (value == null) {
            return "";
        }

        FormatStrings formatStrings = Datatypes.getFormatStrings(locale);
        if (formatStrings == null) {
            return format(value);
        }

        DecimalFormatSymbols formatSymbols = formatStrings.getFormatSymbols();
        NumberFormat format = new DecimalFormat(formatStrings.getIntegerFormat(), formatSymbols);
        return format.format(value);
    }

    @Override
    public Class getJavaClass() {
        return Long.class;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public int getSqlType() {
        return Types.BIGINT;
    }

    @Override
    public Long parse(String value) throws ParseException {
        if (StringUtils.isBlank(value)) {
            return null;
        }

        return parse(value, createFormat()).longValue();
    }

    @Override
    public Long parse(String value, Locale locale) throws ParseException {
        if (StringUtils.isBlank(value)) {
            return null;
        }

        FormatStrings formatStrings = Datatypes.getFormatStrings(locale);
        if (formatStrings == null) {
            return parse(value);
        }

        DecimalFormatSymbols formatSymbols = formatStrings.getFormatSymbols();
        NumberFormat format = new DecimalFormat(formatStrings.getIntegerFormat(), formatSymbols);

        return parse(value, format).longValue();
    }

    @Override
    protected Number parse(String value, NumberFormat format) throws ParseException {
        format.setParseIntegerOnly(true);

        Number result = super.parse(value, format);
        if (!hasValidLongRange(result)) {
            throw new ParseException(String.format("Integer range exceeded: \"%s\"", value), 0);
        }
        return result;
    }

    protected boolean hasValidLongRange(Number result) throws ParseException {
        if (result instanceof Double) {
            Double doubleResult = (Double) result;
            if (doubleResult > Long.MAX_VALUE || doubleResult < Long.MIN_VALUE) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Long read(ResultSet resultSet, int index) throws SQLException {
        Long value = resultSet.getLong(index);
        return resultSet.wasNull() ? null : value;
    }

    @Override
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