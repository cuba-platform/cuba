/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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
 * @author abramov
 * @version $Id$
 */
public interface Datatype<T> {

    /** Unique Datatype name */
    String getName();

    /** Java class representing this Datatype */
    Class getJavaClass();

    /** Converts value to String. Returns not null string for null value.  */
    @Nonnull
    String format(@Nullable T value);

    /** Converts value to String taking into account local formats. Returns not null string for null value. */
    @Nonnull
    String format(@Nullable T value, Locale locale);

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
    void write(PreparedStatement statement, int index, @Nullable T value) throws SQLException;

    /** Corresponding SQL type */
    int getSqlType();
}