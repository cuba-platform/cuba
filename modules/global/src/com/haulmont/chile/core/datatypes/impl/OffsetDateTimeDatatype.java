/*
 * Copyright (c) 2008-2018 Haulmont.
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

import com.haulmont.chile.core.annotations.Ddl;
import com.haulmont.chile.core.annotations.JavaClass;
import com.haulmont.chile.core.datatypes.FormatStrings;
import com.haulmont.chile.core.datatypes.TimeZoneAwareDatatype;
import org.dom4j.Element;

import javax.annotation.Nullable;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.TemporalQuery;
import java.util.Locale;
import java.util.TimeZone;

@JavaClass(OffsetDateTime.class)
@Ddl("timestamp with time zone")
@Ddl(dbms = "mssql", value = "datetimeoffset")
@Ddl(dbms = "mysql", value = "datetime(3)")
public class OffsetDateTimeDatatype extends AbstractTemporalDatatype<OffsetDateTime>
        implements TimeZoneAwareDatatype {

    public OffsetDateTimeDatatype(Element element) {
        super(element);
    }

    @Override
    public String format(@Nullable Object value, Locale locale, TimeZone timeZone) {
        if (timeZone == null || value == null) {
            return format(value, locale);
        }
        OffsetDateTime offsetDateTime = (OffsetDateTime) value;
        ZonedDateTime zonedDateTime = offsetDateTime.atZoneSameInstant(timeZone.toZoneId());
        return format(zonedDateTime, locale);
    }

    @Override
    protected DateTimeFormatter getDateTimeFormatter() {
        return DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
    }

    @Override
    protected DateTimeFormatter getDateTimeFormatter(FormatStrings formatStrings, Locale locale) {
        return DateTimeFormatter.ofPattern(formatStrings.getOffsetDateTimeFormat(), locale);
    }

    @Override
    protected TemporalQuery<OffsetDateTime> newInstance() {
        return OffsetDateTime::from;
    }
}