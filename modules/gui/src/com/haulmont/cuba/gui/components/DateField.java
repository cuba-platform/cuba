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
package com.haulmont.cuba.gui.components;

import com.google.common.reflect.TypeToken;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.util.TimeZone;

public interface DateField<V> extends Field<V>, HasDatatype<V>, Buffered, Component.Focusable, HasRange<V> {
    String NAME = "dateField";

    TypeToken<TextField<Date>> TYPE_DATE = new TypeToken<TextField<java.sql.Date>>(){};
    TypeToken<TextField<java.util.Date>> TYPE_DATETIME = new TypeToken<TextField<java.util.Date>>(){};
    TypeToken<TextField<LocalDate>> TYPE_LOCALDATE = new TypeToken<TextField<LocalDate>>(){};
    TypeToken<TextField<LocalDateTime>> TYPE_LOCALDATETIME = new TypeToken<TextField<LocalDateTime>>(){};
    TypeToken<TextField<java.sql.Time>> TYPE_TIME = new TypeToken<TextField<java.sql.Time>>(){};
    TypeToken<TextField<OffsetTime>> TYPE_OFFSETTIME = new TypeToken<TextField<OffsetTime>>(){};

    enum Resolution {
        SEC,
        MIN,
        HOUR,
        DAY,
        MONTH,
        YEAR
    }

    Resolution getResolution();

    void setResolution(Resolution resolution);

    String getDateFormat();

    void setDateFormat(String dateFormat);

    /**
     * Use {@link DateField#getZoneId()}
     */
    TimeZone getTimeZone();

    /**
     * Use {@link DateField#setZoneId(ZoneId)}
     */
    void setTimeZone(TimeZone timeZone);

    void setZoneId(ZoneId zoneId);

    ZoneId getZoneId();
}