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
import org.dom4j.Element;

import java.time.OffsetTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.TemporalQuery;
import java.util.Locale;

@JavaClass(OffsetTime.class)
@Ddl("time with time zone")
@Ddl(dbms = "oracle", value = "timestamp with time zone")
@Ddl(dbms = "mssql", value = "time")
@Ddl(dbms = "mysql", value = "time(3)")
public class OffsetTimeDatatype extends AbstractTemporalDatatype<OffsetTime> {

    public OffsetTimeDatatype(Element element) {
        super(element);
    }

    @Override
    protected DateTimeFormatter getDateTimeFormatter() {
        return DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
    }

    @Override
    protected DateTimeFormatter getDateTimeFormatter(FormatStrings formatStrings, Locale locale) {
        return DateTimeFormatter.ofPattern(formatStrings.getOffsetTimeFormat(), locale);
    }

    @Override
    protected TemporalQuery<OffsetTime> newInstance() {
        return OffsetTime::from;
    }
}