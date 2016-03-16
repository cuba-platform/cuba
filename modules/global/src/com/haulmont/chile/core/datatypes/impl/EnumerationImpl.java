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

import com.haulmont.chile.core.datatypes.Enumeration;

import java.text.ParseException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.*;

import org.apache.commons.lang.StringUtils;

import javax.annotation.Nonnull;

/**
 * @param <T>
 */
public class EnumerationImpl<T extends Enum> implements Enumeration<T> {

    private Class<T> javaClass;

    public EnumerationImpl(Class<T> javaClass) {
        this.javaClass = javaClass;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Class<T> getJavaClass() {
        return javaClass;
    }

    @Nonnull
    @Override
    public String format(Object value) {
        if (value == null) return "";

        final Object v = ((EnumClass) value).getId();
        return String.valueOf(v);
    }

    @Nonnull
    @Override
    public String format(Object value, Locale locale) {
        return format(value);
    }

    @Override
    public T parse(String value) throws ParseException {
        if (StringUtils.isBlank(value))
            return null;

        for (Enum enumValue : javaClass.getEnumConstants()) {
            Object enumId = ((EnumClass) enumValue).getId();
            if (value.equals(enumId.toString()))
                return (T) enumValue;
        }
        return null;
    }

    @Override
    public T parse(String value, Locale locale) throws ParseException {
        return parse(value);
    }

    @Override
    public T read(ResultSet resultSet, int index) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void write(PreparedStatement statement, int index, Object value) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getSqlType() {
        return Types.INTEGER;
    }

    @Override
    public List<Enum> getValues() {
        final Enum[] enums = javaClass.getEnumConstants();
        return Arrays.asList(enums);
    }

    @Override
    public String toString() {
        return javaClass.getName();
    }
}