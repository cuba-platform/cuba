/*
 * Copyright (c) 2008-2020 Haulmont.
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

package com.haulmont.chile.core.datatypes.impl;

import com.haulmont.chile.core.annotations.JavaClass;
import com.haulmont.chile.core.datatypes.Datatype;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.text.ParseException;
import java.util.Locale;

@JavaClass(Character.class)
public class CharacterDatatype implements Datatype<Character> {
    @Override
    public String format(@Nullable Object value) {
        return value == null ? "" : value.toString();
    }

    @Override
    public String format(@Nullable Object value, Locale locale) {
        return format(value);
    }

    @Nullable
    @Override
    public Character parse(@Nullable String value) throws ParseException {
        if (StringUtils.isBlank(value))
            return null;

        if (value.length() > 1)
            throw new ParseException(String.format("String '%s' is too long", value), 0);

        return value.charAt(0);
    }

    @Nullable
    @Override
    public Character parse(@Nullable String value, Locale locale) throws ParseException {
        return parse(value);
    }


}
