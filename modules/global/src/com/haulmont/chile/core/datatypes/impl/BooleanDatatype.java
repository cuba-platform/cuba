/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 */
public class BooleanDatatype implements Datatype<Boolean> {

    public final static String NAME = "boolean";

    @Nonnull
    @Override
    public String format(Object value) {
        return value == null ? "" : Boolean.toString((Boolean) value);
    }

    @Nonnull
    @Override
    public String format(Object value, Locale locale) {
        if (value == null) {
            return "";
        }

        FormatStrings formatStrings = Datatypes.getFormatStrings(locale);
        if (formatStrings == null) {
            return format(value);
        }

        return (boolean) value ? formatStrings.getTrueString() : formatStrings.getFalseString();
    }

    @Override
    public Class getJavaClass() {
        return Boolean.class;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public int getSqlType() {
        return Types.BOOLEAN;
    }

    protected Boolean parse(@Nullable String value, String trueString, String falseString) throws ParseException {
        if (!StringUtils.isBlank(value)) {
            String lowerCaseValue = StringUtils.lowerCase(value);
            if (trueString.equals(lowerCaseValue)) {
                return true;
            }
            if (falseString.equals(lowerCaseValue)) {
                return false;
            }
            throw new ParseException(String.format("Can't parse '%s'", value), 0);
        }
        return null;
    }

    @Override
    public Boolean parse(String value) throws ParseException {
        return parse(value, "true", "false");
    }

    @Override
    public Boolean parse(String value, Locale locale) throws ParseException {
        if (StringUtils.isBlank(value)) {
            return null;
        }

        FormatStrings formatStrings = Datatypes.getFormatStrings(locale);
        if (formatStrings == null) {
            return parse(value);
        }

        return parse(value, formatStrings.getTrueString(), formatStrings.getFalseString());
    }

    @Override
    public Boolean read(ResultSet resultSet, int index) throws SQLException {
        Boolean value = resultSet.getBoolean(index);
        return resultSet.wasNull() ? null : value;
    }

    @Override
    public void write(PreparedStatement statement, int index, Object value) throws SQLException {
        if (value == null) {
            statement.setString(index, null);
        } else {
            statement.setBoolean(index, (Boolean) value);
        }
    }

    @Override
    public String toString() {
        return NAME;
    }
}