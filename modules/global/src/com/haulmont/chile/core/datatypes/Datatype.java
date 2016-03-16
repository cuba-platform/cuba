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

package com.haulmont.chile.core.datatypes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Locale;

/**
 * Represents a data type of entity property.
 *
 */
public interface Datatype<T> {

    /** Unique Datatype name */
    String getName();

    /** Java class representing this Datatype */
    Class getJavaClass();

    /** Converts value to String. Returns an empty string for null value.  */
    @Nonnull
    String format(@Nullable Object value);

    /** Converts value to String taking into account local formats. Returns an empty string for null value. */
    @Nonnull
    String format(@Nullable Object value, Locale locale);

    /** Parses value from String */
    @Nullable
    T parse(@Nullable String value) throws ParseException;

    /** Parses value from String taking into account local formats */
    @Nullable
    T parse(@Nullable String value, Locale locale) throws ParseException;

    /** Reads value from JDBC ResultSet */
    @Nullable
    T read(ResultSet resultSet, int index) throws SQLException;

    /** Write value to the JDBC Statement*/
    void write(PreparedStatement statement, int index, @Nullable Object value) throws SQLException;

    /** Corresponding SQL type */
    int getSqlType();
}