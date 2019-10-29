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
import java.time.*;
import java.util.TimeZone;

/**
 * A date entry component, which displays the actual date selector or date with time.
 *
 * @param <V> type of value
 */
public interface DateField<V> extends Field<V>, HasDatatype<V>, Buffered, Component.Focusable, HasRange<V> {
    String NAME = "dateField";

    TypeToken<DateField<Date>> TYPE_DEFAULT = new TypeToken<DateField<Date>>(){};

    TypeToken<DateField<Date>> TYPE_DATE = new TypeToken<DateField<java.sql.Date>>(){};
    TypeToken<DateField<java.util.Date>> TYPE_DATETIME = new TypeToken<DateField<java.util.Date>>(){};
    TypeToken<DateField<LocalDate>> TYPE_LOCALDATE = new TypeToken<DateField<LocalDate>>(){};
    TypeToken<DateField<LocalDateTime>> TYPE_LOCALDATETIME = new TypeToken<DateField<LocalDateTime>>(){};
    TypeToken<DateField<OffsetDateTime>> TYPE_OFFSETDATETIME = new TypeToken<DateField<OffsetDateTime>>(){};

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

    /**
     * Sets whether autofill feature is enabled.
     * <p>
     * When enabled uses current month and year.
     *
     * @param autofill whether autofill is enabled
     */
    void setAutofill(boolean autofill);

    /**
     * @return whether autofill is enabled
     */
    boolean isAutofill();

    /**
     * Sets time mode to use (12h AM/PM or 24h).
     * <p>
     * By default the 24h mode is used.
     *
     * @param timeMode time mode
     */
    void setTimeMode(TimeField.TimeMode timeMode);

    /**
     * @return {@link TimeField.TimeMode} that is used by component
     */
    TimeField.TimeMode getTimeMode();
}