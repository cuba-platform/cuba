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
 */

package com.haulmont.cuba.restapi;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.cuba.core.entity.Entity;

import java.math.BigDecimal;
import java.sql.Time;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

public final class ParseUtils {

    private ParseUtils() {
    }

    /**
     * Tries to parse a string value into some of the available Datatypes
     * when no Datatype was specified.
     *
     * @param value value to parse
     * @return parsed value
     */
    public static Object tryParse(String value) {
        try {
            if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
                return parseByDatatype(value, Boolean.class);
            }
        } catch (ParseException ignored) {
        }
        try {
            return parseByDatatype(value, UUID.class);
        } catch (Exception ignored) {
        }
        try {
            return parseByDatatype(value, Date.class);
        } catch (ParseException ignored) {
        }
        try {
            return parseByDatatype(value, Time.class);
        } catch (ParseException ignored) {
        }
        try {
            return parseByDatatype(value, java.sql.Date.class);
        } catch (ParseException ignored) {
        }
        try {
            return parseByDatatype(value, BigDecimal.class);
        } catch (ParseException ignored) {
        }
        try {
            return parseByDatatype(value, Double.class);
        } catch (ParseException ignored) {
        }
        //return string value if couldn't parse into specific type
        return value;
    }

    private static Object parseByDatatype(String value, Class<?> type) throws ParseException {
        Datatype datatype = Datatypes.getNN(type);
        return datatype.parse(value);
    }

    /**
     * Parses string value into specific type
     *
     * @param value    value to parse
     * @param typeName Datatype name
     * @return parsed object
     */
    public static Object parseByTypename(String value, String typeName) {
        Datatype datatype = Datatypes.get(typeName);
        try {
            return datatype.parse(value);
        } catch (ParseException e) {
            throw new IllegalArgumentException(String.format("Cannot parse specified parameter of type '%s'", typeName), e);
        }
    }

    /**
     * Parse string value into object with specific class
     *
     * @param clazz     needed required class
     * @param value     value to parse
     * @param converter which converter calls this method
     * @return parsed object
     */
    public static Object toObject(Class clazz, String value, Converter converter) throws ParseException {
        if (String.class == clazz) {
            return value;
        }
        if (Integer.class == clazz || Integer.TYPE == clazz
                || Byte.class == clazz || Byte.TYPE == clazz
                || Short.class == clazz || Short.TYPE == clazz) {
            return Datatypes.getNN(Integer.class).parse(value);
        }
        if (Date.class == clazz) {
            try {
                return Datatypes.getNN(Date.class).parse(value);
            } catch (ParseException e) {
                try {
                    return Datatypes.getNN(java.sql.Date.class).parse(value);
                } catch (ParseException e1) {
                    return Datatypes.getNN(Time.class).parse(value);
                }
            }
        }
        if (BigDecimal.class == clazz) {
            return Datatypes.getNN(BigDecimal.class).parse(value);
        }
        if (Boolean.class == clazz || Boolean.TYPE == clazz) {
            return Datatypes.getNN(Boolean.class).parse(value);
        }
        if (Long.class == clazz || Long.TYPE == clazz) {
            return Datatypes.getNN(Long.class).parse(value);
        }
        if (Double.class == clazz || Double.TYPE == clazz
                || Float.class == clazz || Float.TYPE == clazz) {
            return Datatypes.getNN(Double.class).parse(value);
        }
        if (UUID.class == clazz) {
            return UUID.fromString(value);
        }
        if (Entity.class.isAssignableFrom(clazz)) {
            return converter.parseEntity(value);
        }
        if (Collection.class.isAssignableFrom(clazz)) {
            return converter.parseEntitiesCollection(value, clazz);
        }
        throw new IllegalArgumentException("Parameters of type " + clazz.getName() + " are not supported");
    }
}