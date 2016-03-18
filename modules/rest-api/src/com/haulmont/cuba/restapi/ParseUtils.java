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
import com.haulmont.chile.core.datatypes.impl.*;
import com.haulmont.cuba.core.entity.Entity;

import java.math.BigDecimal;
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
            return parseByDatatypeName(value, UUIDDatatype.NAME);
        } catch (Exception ignored) {
        }
        try {
            return parseByDatatypeName(value, DateTimeDatatype.NAME);
        } catch (ParseException ignored) {
        }
        try {
            return parseByDatatypeName(value, TimeDatatype.NAME);
        } catch (ParseException ignored) {
        }
        try {
            return parseByDatatypeName(value, DateDatatype.NAME);
        } catch (ParseException ignored) {
        }
        try {
            return parseByDatatypeName(value, BigDecimalDatatype.NAME);
        } catch (ParseException ignored) {
        }
        try {
            return parseByDatatypeName(value, DoubleDatatype.NAME);
        } catch (ParseException ignored) {
        }
        try {
            if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
                return parseByDatatypeName(value, BooleanDatatype.NAME);
            }
        } catch (ParseException ignored) {
        }
        //return string value if couldn't parse into specific type
        return value;
    }

    private static Object parseByDatatypeName(String value, String name) throws ParseException {
        Datatype datatype = Datatypes.get(name);
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
     * @param convertor which convertor calls this method
     * @return parsed object
     */
    public static Object toObject(Class clazz, String value, Convertor convertor) throws ParseException {
        if (String.class == clazz) {
            return value;
        }
        if (Integer.class == clazz || Integer.TYPE == clazz
                || Byte.class == clazz || Byte.TYPE == clazz
                || Short.class == clazz || Short.TYPE == clazz) {
            return Datatypes.get(IntegerDatatype.NAME).parse(value);
        }
        if (Date.class == clazz) {
            try {
                return Datatypes.get(DateTimeDatatype.NAME).parse(value);
            } catch (ParseException e) {
                try {
                    return Datatypes.get(DateDatatype.NAME).parse(value);
                } catch (ParseException e1) {
                    return Datatypes.get(TimeDatatype.NAME).parse(value);
                }
            }
        }
        if (BigDecimal.class == clazz) {
            return Datatypes.get(BigDecimalDatatype.NAME).parse(value);
        }
        if (Boolean.class == clazz || Boolean.TYPE == clazz) {
            return Datatypes.get(BooleanDatatype.NAME).parse(value);
        }
        if (Long.class == clazz || Long.TYPE == clazz) {
            return Datatypes.get(LongDatatype.NAME).parse(value);
        }
        if (Double.class == clazz || Double.TYPE == clazz
                || Float.class == clazz || Float.TYPE == clazz) {
            return Datatypes.get(DoubleDatatype.NAME).parse(value);
        }
        if (UUID.class == clazz) {
            return UUID.fromString(value);
        }
        if (Entity.class.isAssignableFrom(clazz)) {
            return convertor.parseEntity(value);
        }
        if (Collection.class.isAssignableFrom(clazz)) {
            return convertor.parseEntitiesCollection(value, clazz);
        }
        throw new IllegalArgumentException("Parameters of type " + clazz.getName() + " are not supported");
    }
}