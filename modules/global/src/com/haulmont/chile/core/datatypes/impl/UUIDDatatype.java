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

import com.haulmont.chile.core.annotations.JavaClass;
import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.cuba.core.global.UuidProvider;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.util.Locale;
import java.util.UUID;

@JavaClass(UUID.class)
public class UUIDDatatype implements Datatype<UUID> {

    @Override
    public String format(Object value) {
        return value == null ? "" : value.toString();
    }

    @Override
    public String format(Object value, Locale locale) {
        return format(value);
    }

    @Override
    public UUID parse(String value) throws ParseException {
        if (StringUtils.isBlank(value)) {
            return null;
        } else {
            try {
                return UuidProvider.fromString(value.trim());
            } catch (Exception e) {
                throw new ParseException("Error parsing UUID", 0);
            }
        }
    }

    @Override
    public UUID parse(String value, Locale locale) throws ParseException {
        return parse(value);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    @Deprecated
    public final static String NAME = "uuid";
}