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

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.chile.core.annotations.JavaClass;
import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.FormatStrings;
import com.haulmont.chile.core.datatypes.FormatStringsRegistry;
import com.haulmont.chile.core.datatypes.ParameterizedDatatype;
import com.haulmont.cuba.core.global.AppBeans;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

/**
 * <code>TimeDatatype</code> works with <code>java.sql.Time</code> but is parametrized with <code>java.util.Date</code>
 * to avoid problems with casting, e.g. <code>org.apache.openjpa.util.java$util$Date$proxy</code>.
 */
@JavaClass(Time.class)
public class TimeDatatype implements Datatype<Date>, ParameterizedDatatype {

    private String formatPattern;

    public TimeDatatype(Element element) {
        formatPattern = element.attributeValue("format");
    }

    @Override
    public String format(Object value) {
        if (value == null) {
            return "";
        } else {
            DateFormat format;
            if (formatPattern != null) {
                format = new SimpleDateFormat(formatPattern);
            } else {
                format = DateFormat.getTimeInstance();
            }
            format.setLenient(false);
            return format.format(value);
        }
    }

    @Override
    public String format(Object value, Locale locale) {
        if (value == null) {
            return "";
        }

        FormatStrings formatStrings = AppBeans.get(FormatStringsRegistry.class).getFormatStrings(locale);
        if (formatStrings == null) {
            return format(value);
        }

        DateFormat format = new SimpleDateFormat(formatStrings.getTimeFormat());
        format.setLenient(false);

        return format.format(value);
    }

    @Override
    public Date parse(String value) throws ParseException {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        DateFormat format;
        if (formatPattern != null) {
            format = new SimpleDateFormat(formatPattern);
        } else {
            format = DateFormat.getTimeInstance();
        }

        return format.parse(value.trim());
    }

    @Override
    public Date parse(String value, Locale locale) throws ParseException {
        if (StringUtils.isBlank(value)) {
            return null;
        }

        FormatStrings formatStrings = AppBeans.get(FormatStringsRegistry.class).getFormatStrings(locale);
        if (formatStrings == null) {
            return parse(value);
        }

        DateFormat format = new SimpleDateFormat(formatStrings.getTimeFormat());
        return format.parse(value.trim());
    }

    @Override
    public Map<String, Object> getParameters() {
        return ParamsMap.of("format", formatPattern);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    @Deprecated
    public final static String NAME = "time";
}