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
import org.apache.commons.codec.binary.Base64;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Locale;

@JavaClass(byte[].class)
public class ByteArrayDatatype implements Datatype<byte[]> {

    @Override
    public String format(Object value) {
        if (value == null)
            return "";

        return new String(Base64.encodeBase64((byte[]) value), StandardCharsets.UTF_8);
    }

    @Override
    public String format(Object value, Locale locale) {
        if (value == null)
            return "";

        return format(value);
    }

    @Override
    public byte[] parse(String value) throws ParseException {
        if (value == null || value.length() == 0)
            return null;

        return Base64.decodeBase64(value.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public byte[] parse(String value, Locale locale) throws ParseException {
        return parse(value);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    @Deprecated
    public static final String NAME = "byteArray";
}