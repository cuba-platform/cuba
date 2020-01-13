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

package com.haulmont.cuba.core.global;

import com.google.common.base.Preconditions;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.time.*;
import java.util.Date;

@Component(DateTimeTransformations.NAME)
public class DateTimeTransformations {
    public static final String NAME = "cuba_DateTimeTransformations";

    /**
     * Converts a date instance to the passed java type corresponding to one of the date types.
     *
     * @param date     the date object, not {@code null}
     * @param javaType the java type to convert to
     * @param zoneId   the zone ID to use or {@code null} to use default system timezone
     * @return the date object converted to the passed java type, not {@code null}
     */
    public Object transformToType(Object date, Class javaType, @Nullable ZoneId zoneId) {
        Preconditions.checkNotNull(date);
        Preconditions.checkNotNull(javaType);

        ZonedDateTime zonedDateTime = transformToZDT(date, zoneId);
        return transformFromZdtInternal(zonedDateTime, javaType);
    }

    /**
     * Obtains an instance of ZonedDateTime
     * from Date or LocalDate or LocalDateTime or OffsetDateTime
     * ZonedDateTime is created for LocalDate, LocalDateTime with default system timezone
     * @param date date object, not null
     * @return the ZonedDateTime, not null
     */
    public ZonedDateTime transformToZDT(Object date) {
        return transformToZDT(date, null);
    }

    protected ZonedDateTime transformToZDT(Object date, @Nullable ZoneId fromZoneId) {
        Preconditions.checkNotNull(date);
        ZoneId zoneId = fromZoneId != null ? fromZoneId : ZoneId.systemDefault();
        if (date instanceof java.sql.Date) {
            return ((java.sql.Date) date).toLocalDate().atStartOfDay(zoneId);
        } else if (date instanceof Date) {
            return ((Date) date).toInstant().atZone(zoneId);
        } else if (date instanceof LocalDate) {
            return ((LocalDate) date).atStartOfDay(zoneId);
        } else if (date instanceof LocalDateTime) {
            return ((LocalDateTime) date).atZone(zoneId);
        } else if (date instanceof OffsetDateTime) {
            return ((OffsetDateTime) date).atZoneSameInstant(zoneId);
        }
        throw newUnsupportedTypeException(date.getClass());
    }

    /**
     * Obtains an instance of specified by type date object from
     * from ZonedDateTime
     * LocalDate, LocalDateTime is created for default system timezone
     * @param zonedDateTime date object, not null
     * @param javaType date type to transformation from ZonedDateTime
     * @return the date object, not null
     */
    public Object transformFromZDT(ZonedDateTime zonedDateTime, Class javaType) {
        Preconditions.checkNotNull(zonedDateTime);
        zonedDateTime = zonedDateTime.withZoneSameInstant(ZoneId.systemDefault());
        return transformFromZdtInternal(zonedDateTime, javaType);
    }

    protected Object transformFromZdtInternal(ZonedDateTime zonedDateTime, Class javaType) {
        if (java.sql.Date.class.equals(javaType)) {
            return java.sql.Date.valueOf(zonedDateTime.toLocalDate());
        } else if (Date.class.equals(javaType)) {
            return Date.from(zonedDateTime.toInstant());
        } else if (LocalDate.class.equals(javaType)) {
            return zonedDateTime.toLocalDate();
        } else if (LocalDateTime.class.equals(javaType)) {
            return zonedDateTime.toLocalDateTime();
        } else if (OffsetDateTime.class.equals(javaType)) {
            return zonedDateTime.toOffsetDateTime();
        }
        throw newUnsupportedTypeException(javaType);
    }

    /**
     * Obtains an instance of LocalTime
     * from Time or Date or LocalTime or OffsetTime
     * @param date date object, not null
     * @return the LocalTime, not null
     */
    public LocalTime transformToLocalTime(Object date) {
        Preconditions.checkNotNull(date);
        if (date instanceof java.sql.Time) {
            return ((java.sql.Time) date).toLocalTime();
        } else if (date instanceof Date) {
            return ((Date) date).toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalTime();
        } else if (date instanceof LocalTime) {
            return (LocalTime) date;
        } else if (date instanceof OffsetTime) {
            return ((OffsetTime) date).toLocalTime();
        }
        throw newUnsupportedTypeException(date.getClass());
    }

    /**
     * Obtains an instance of specified by type date object from
     * from LocalTime
     * @param localTime date object, not null
     * @param javaType date type to transformation from LocalTime
     * @return the date object, not null
     */
    public Object transformFromLocalTime(LocalTime localTime, Class javaType) {
        if (java.sql.Time.class.equals(javaType)) {
            return java.sql.Time.valueOf(localTime);
        } else if (Date.class.equals(javaType)) {
            return new Date(java.sql.Time.valueOf(localTime).getTime());
        } else if (LocalTime.class.equals(javaType)) {
            return localTime;
        } else if (OffsetTime.class.equals(javaType)) {
            return new Date(java.sql.Time.valueOf(localTime).getTime())
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toOffsetDateTime()
                    .toOffsetTime();
        }
        throw newUnsupportedTypeException(javaType);
    }

    /**
     * Check if date type supports time zone conversation
     * @param javaType - date type
     * @return true - if date type supports timezones
     */
    public boolean isDateTypeSupportsTimeZones(Class javaType) {
        return Date.class.equals(javaType) || OffsetDateTime.class.equals(javaType);
    }

    private static RuntimeException newUnsupportedTypeException(Class javaType) {
        throw new IllegalArgumentException(String.format("Unsupported date type %s", javaType));
    }
}
