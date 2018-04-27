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

package com.haulmont.bali.util;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

public class DateTimeUtils {

    private DateTimeUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * Obtains an instance of {@code LocalTime} from a date object.
     * <p>
     * If the date object is an instance of {@link java.sql.Time}, then {@link java.sql.Time#toLocalTime()} is used.
     * Otherwise {@code LocalTime} is obtained by converting {@code Instant} at {@link ZoneId#systemDefault()}
     * to {@code LocalTime}.
     *
     * @param date the date object to convert, not null
     * @return the local time, not null
     */
    public static LocalTime asLocalTime(Date date) {
        return asLocalTime(date, getDefaultTimeZone());
    }

    /**
     * Obtains an instance of {@code LocalTime} from a date object.
     * <p>
     * If the date object is an instance of {@link java.sql.Time}, then {@link java.sql.Time#toLocalTime()} is used.
     * Otherwise {@code LocalTime} is obtained by converting {@code Instant} at {@link ZoneId#systemDefault()}
     * to {@code LocalTime}.
     *
     * @param date   the date object to convert, not null
     * @param zoneId the time zone id, not null
     * @return the local time, not null
     */
    public static LocalTime asLocalTime(Date date, ZoneId zoneId) {
        return date instanceof Time ? ((Time) date).toLocalTime()
                : date.toInstant().atZone(zoneId).toLocalTime();
    }

    /**
     * Obtains an instance of {@code LocalDate} from a date object.
     * <p>
     * If the date object is an instance of {@link java.sql.Date}, then {@link java.sql.Date#toLocalDate()} is used.
     * Otherwise {@code LocalDate} is obtained by converting {@code Instant} at {@link ZoneId#systemDefault()}
     * to {@code LocalDate}.
     *
     * @param date the date object to convert, not null
     * @return the local date, not null
     */
    public static LocalDate asLocalDate(Date date) {
        return asLocalDate(date, getDefaultTimeZone());
    }

    /**
     * Obtains an instance of {@code LocalDate} from a date object.
     * <p>
     * If the date object is an instance of {@link java.sql.Date}, then {@link java.sql.Date#toLocalDate()} is used.
     * Otherwise {@code LocalDate} is obtained by converting {@code Instant} at {@link ZoneId#systemDefault()}
     * to {@code LocalDate}.
     *
     * @param date   the date object to convert, not null
     * @param zoneId the time zone id, not null
     * @return the local date, not null
     */
    public static LocalDate asLocalDate(Date date, ZoneId zoneId) {
        return date instanceof java.sql.Date ? ((java.sql.Date) date).toLocalDate()
                : date.toInstant().atZone(zoneId).toLocalDate();
    }

    /**
     * Obtains an instance of {@code LocalDateTime} from a date object.
     * <p>
     * {@code LocalDate} is obtained by converting {@code Instant} at {@link ZoneId#systemDefault()}
     * to {@code LocalDateTime}.
     *
     * @param date the date object to convert, not null
     * @return the local date-time, not null
     */
    public static LocalDateTime asLocalDateTime(Date date) {
        return asLocalDateTime(date, getDefaultTimeZone());
    }

    /**
     * Obtains an instance of {@code LocalDateTime} from a date object.
     * <p>
     * {@code LocalDate} is obtained by converting {@code Instant} at {@link ZoneId#systemDefault()}
     * to {@code LocalDateTime}.
     *
     * @param date   the date object to convert, not null
     * @param zoneId the time zone id, not null
     * @return the local date-time, not null
     */
    public static LocalDateTime asLocalDateTime(Date date, ZoneId zoneId) {
        return date.toInstant().atZone(zoneId).toLocalDateTime();
    }

    /**
     * Obtains an instance of {@code Date} from a local time object.
     * <p>
     * A {@code Date} object represents the current date with the time represented by {@code LocalTime}.
     *
     * @param localTime the local time object, not null
     * @return the date, not null
     */
    public static Date asDate(LocalTime localTime) {
        return asDate(localTime, getDefaultTimeZone());
    }

    /**
     * Obtains an instance of {@code Date} from a local time object.
     * <p>
     * A {@code Date} object represents date represented by {@code LocalDate}
     * with time represented by {@code LocalTime}.
     *
     * @param localTime the local time object, not null
     * @param localDate the local date object, not null
     * @return the date, not null
     */
    public static Date asDate(LocalTime localTime, LocalDate localDate) {
        return asDate(localTime, localDate, getDefaultTimeZone());
    }

    /**
     * Obtains an instance of {@code Date} from a local time object.
     * <p>
     * A {@code Date} object represents the current date with the time represented by {@code LocalTime}.
     *
     * @param localTime the local time object, not null
     * @param zoneId    the time zone id, not null
     * @return the date, not null
     */
    public static Date asDate(LocalTime localTime, ZoneId zoneId) {
        return asDate(localTime, LocalDate.now(), zoneId);
    }

    /**
     * Obtains an instance of {@code Date} from a local time object.
     * <p>
     * A {@code Date} object represents the date represented by {@code LocalDate}
     * with the time represented by {@code LocalTime}.
     *
     * @param localTime the local time object, not null
     * @param localDate the local date object, not null
     * @param zoneId    the time zone id, not null
     * @return the date, not null
     */
    public static Date asDate(LocalTime localTime, LocalDate localDate, ZoneId zoneId) {
        return Date.from(localTime.atDate(localDate).atZone(zoneId).toInstant());
    }

    /**
     * Obtains an instance of {@code Date} from a local date object.
     *
     * @param localDate the local date object, not null
     * @return the date, not null
     */
    public static Date asDate(LocalDate localDate) {
        return asDate(localDate, getDefaultTimeZone());
    }

    /**
     * Obtains an instance of {@code Date} from a local date object.
     *
     * @param localDate the local date object, not null
     * @param zoneId    the time zone id, not null
     * @return the date, not null
     */
    public static Date asDate(LocalDate localDate, ZoneId zoneId) {
        return Date.from(localDate.atStartOfDay(zoneId).toInstant());
    }

    /**
     * Obtains an instance of {@code Date} from a local date-time object.
     *
     * @param localDateTime the local date-time object, not null
     * @return the date, not null
     */
    public static Date asDate(LocalDateTime localDateTime) {
        return asDate(localDateTime, getDefaultTimeZone());
    }

    /**
     * Obtains an instance of {@code Date} from a local date-time object.
     *
     * @param localDateTime the local date-time object, not null
     * @param zoneId        the time zone id, not null
     * @return the date, not null
     */
    public static Date asDate(LocalDateTime localDateTime, ZoneId zoneId) {
        return Date.from(localDateTime.atZone(zoneId).toInstant());
    }

    /**
     * Returns a date with zero time.
     *
     * @param date the date object, not null
     * @return the date, not null
     */
    public static Date getDateWithoutTime(Date date) {
        return asDate(asLocalDate(date));
    }

    /**
     * Checks if the date object represented by {@code input} is within the range
     * represented by {@code startDate} and {@code endDate} inclusive.
     *
     * @param input     a date object to test, not null
     * @param startDate a date object corresponding to the lower boundary, not null
     * @param endDate   a date object corresponding to the upper boundary, not null
     * @return {@code true} if the date object represented by {@code input} is within
     * the range represented by {@code startDate} and {@code endDate} inclusive
     */
    public static boolean isWithinRange(Date input, Date startDate, Date endDate) {
        if (startDate.after(endDate)) {
            throw new IllegalArgumentException("The start date must be earlier or equals to the end date");
        }
        return !(input.before(startDate) || input.after(endDate));
    }

    /**
     * Checks if a {@code input} is <i>not</i> within the range
     * represented by {@code startDate} and {@code endDate}.
     *
     * @param input     a date object to test, not null
     * @param startDate a date object corresponding to the lower boundary, not null
     * @param endDate   a date object corresponding to the upper boundary, not null
     * @return {@code true} if the date object represented by {@code input} is <i>not</i>
     * within the range represented by {@code startDate} and {@code endDate} inclusive
     */
    public static boolean isNotWithinRange(Date input, Date startDate, Date endDate) {
        return !isWithinRange(input, startDate, endDate);
    }

    private static ZoneId getDefaultTimeZone() {
        // TODO: gg, return proper ZoneId
        return ZoneId.systemDefault();
    }
}
