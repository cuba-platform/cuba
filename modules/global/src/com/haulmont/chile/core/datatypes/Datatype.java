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

import com.haulmont.chile.core.annotations.JavaClass;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Locale;

/**
 * Represents a data type of an entity property.
 */
public interface Datatype<T> {

    /** Converts value to String. Returns an empty string for null value.  */
    String format(@Nullable Object value);

    /** Converts value to String taking into account local formats. Returns an empty string for null value. */
    String format(@Nullable Object value, Locale locale);

    /**
     * Parses value from String. During the parsing process, you can throw {@link ValueConversionException}
     * instead of {@link ParseException}.
     *
     * @param value value to parse
     */
    @Nullable
    T parse(@Nullable String value) throws ParseException;

    /**
     * Parses value from String taking into account local formats. During the parsing process, you can throw
     * {@link ValueConversionException} instead of {@link ParseException}.
     *
     * @param locale locale
     * @param value  value to parse
     */
    @Nullable
    T parse(@Nullable String value, Locale locale) throws ParseException;

    /** Java class representing this Datatype */
    default Class getJavaClass() {
        JavaClass annotation = getClass().getAnnotation(JavaClass.class);
        if (annotation == null)
            throw new IllegalStateException("Datatype " + this + " does not declare a Java class it works with. " +
                    "Either add @JavaClass annotation or implement getJavaClass() method.");
        return annotation.value();
    }

    /**
     * DEPRECATED.
     * Use {@link DatatypeRegistry#getId(Datatype)} or {@link DatatypeRegistry#getIdByJavaClass(Class)} methods.
     */
    @Deprecated
    default String getName() {
        try {
            Field nameField = getClass().getField("NAME");
            if (Modifier.isStatic(nameField.getModifiers())) {
                return (String) nameField.get(null);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            // ignore
        }
        throw new IllegalStateException("Cannot get datatype name. Do not use this method as it is deprecated.");
    }

    @Deprecated
    @Nullable
    default T read(ResultSet resultSet, int index) throws SQLException {
        throw new UnsupportedOperationException("Method is deprecated");
    }

    @Deprecated
    default void write(PreparedStatement statement, int index, @Nullable Object value) throws SQLException {
        throw new UnsupportedOperationException("Method is deprecated");
    }

    @Deprecated
    default int getSqlType() {
        throw new UnsupportedOperationException("Method is deprecated");
    }
}