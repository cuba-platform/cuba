/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.chile.core.datatypes;

import java.sql.ResultSet;
import java.sql.PreparedStatement;
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

    /** Converts value to String. Returns null for null value.  */
    String format(T value);

    /** Converts value to String taking into account local formats. Returns empty string for null value. */
    String format(T value, Locale locale);

    /** Parses value from String */
    T parse(String value) throws ParseException;

    /** Parses value from String taking into account local formats */
    T parse(String value, Locale locale) throws ParseException;

    /** Reads value from JDBC ResultSet */
    T read(ResultSet resultSet, int index) throws SQLException;

    /** Write value to the JDBC Statement*/
    void write(PreparedStatement statement, int index, T value) throws SQLException;

    /** Corresponding SQL type */
    int getSqlType();
}
