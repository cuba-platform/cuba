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

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.cuba.core.global.UuidProvider;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nonnull;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Locale;
import java.util.UUID;

/**
 */
public class UUIDDatatype implements Datatype<UUID> {

    public final static String NAME = "uuid";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Class getJavaClass() {
        return UUID.class;
    }

    @Nonnull
    @Override
    public String format(Object value) {
        return value == null ? "" : value.toString();
    }

    @Override
    @Nonnull
    public String format(Object value, Locale locale) {
        return format(value);
    }

    @Override
    public UUID parse(String value) throws ParseException {
        return StringUtils.isBlank(value) ? null : UuidProvider.fromString(value.trim());
    }

    @Override
    public UUID parse(String value, Locale locale) throws ParseException {
        return parse(value);
    }

    @Override
    public UUID read(ResultSet resultSet, int index) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void write(PreparedStatement statement, int index, Object value) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getSqlType() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return NAME;
    }
}