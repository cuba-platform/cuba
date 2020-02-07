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

package com.haulmont.cuba.core.global;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.DatatypeRegistry;
import com.haulmont.chile.core.datatypes.impl.DateTimeDatatype;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.sql.Time;
import java.text.ParseException;
import java.time.*;
import java.util.Date;
import java.util.TimeZone;

/**
 * Convenience bean for locale-dependent conversion of some widely used data types to and from strings.
 * <p>
 * For locale-independent conversion use {@link com.haulmont.chile.core.datatypes.Datatype} methods directly.
 */
@Component(DatatypeFormatter.NAME)
public class DatatypeFormatter {

    public static final String NAME = "cuba_DatatypeFormatter";

    @Inject
    protected UserSessionSource uss;
    @Inject
    protected DatatypeRegistry datatypeRegistry;

    /**
     * Format Date (date without time) using {@code dateFormat} string specified in the main message pack.
     * @return string representation or empty string if the value is null
     */
    public String formatDate(@Nullable Date value) {
        return datatypeRegistry.getNN(java.sql.Date.class).format(value, uss.getLocale());
    }

    /**
     * Format Date (time without date) using {@code timeFormat} string specified in the main message pack.
     * @return string representation or empty string if the value is null
     */
    public String formatTime(@Nullable Date value) {
        return datatypeRegistry.getNN(Time.class).format(value, uss.getLocale());
    }

    /**
     * Format Date (date and time) using {@code dateTimeFormat} string specified in the main message pack.
     * <p>Takes into account time zone if it is set for the current user session.</p>
     * @return string representation or empty string if the value is null
     */
    public String formatDateTime(@Nullable Date value) {
        TimeZone timeZone = uss.getUserSession().getTimeZone();
        Datatype<Date> datatype = datatypeRegistry.getNN(Date.class);
        if (datatype instanceof DateTimeDatatype) {
            return ((DateTimeDatatype) datatype).format(value, uss.getLocale(), timeZone);
        }
        return datatype.format(value, uss.getLocale());
    }

    /**
     * Format LocalDate (date without time and without a time-zone) using {@code dateFormat} string specified in the
     * main message pack.
     *
     * @return string representation or empty string if the value is null
     */
    public String formatLocalDate(@Nullable LocalDate value) {
        return datatypeRegistry.getNN(LocalDate.class).format(value, uss.getLocale());
    }

    /**
     * Format LocalDateTime (date and time without a time-zone) using {@code dateTimeFormat} string specified in the
     * main message pack.
     *
     * @return string representation or empty string if the value is null
     */
    public String formatLocalDateTime(@Nullable LocalDateTime value) {
        return datatypeRegistry.getNN(LocalDateTime.class).format(value, uss.getLocale());
    }

    /**
     * Format LocalTime (time without date and without a time-zone) using {@code timeFormat} string specified in the
     * main message pack.
     *
     * @return string representation or empty string if the value is null
     */
    public String formatLocalTime(@Nullable LocalTime value) {
        return datatypeRegistry.getNN(LocalTime.class).format(value, uss.getLocale());
    }

    /**
     * Format OffsetDateTime (date and time with an offset from UTC/Greenwich) using {@code offsetDateTimeFormat} string
     * specified in the main message pack.
     *
     * @return string representation or empty string if the value is null
     */
    public String formatOffsetDateTime(@Nullable OffsetDateTime value) {
        return datatypeRegistry.getNN(OffsetDateTime.class).format(value, uss.getLocale());
    }

    /**
     * Format OffsetTime (time with an offset from UTC/Greenwich) using {@code offsetTimeFormat} string specified in the
     * main message pack.
     *
     * @return string representation or empty string if the value is null
     */
    public String formatOffsetTime(@Nullable OffsetTime value) {
        return datatypeRegistry.getNN(OffsetTime.class).format(value, uss.getLocale());
    }

    /**
     * Format Double using {@code doubleFormat} string specified in the main message pack.
     * @return string representation or empty string if the value is null
     */
    public String formatDouble(@Nullable Double value) {
        return datatypeRegistry.getNN(Double.class).format(value, uss.getLocale());
    }

    /**
     * Format BigDecimal using {@code decimalFormat} string specified in the main message pack.
     * @return string representation or empty string if the value is null
     */
    public String formatBigDecimal(@Nullable BigDecimal value) {
        return datatypeRegistry.getNN(BigDecimal.class).format(value, uss.getLocale());
    }

    /**
     * Format Boolean using {@code trueString} and {@code falseString} strings specified in the main message pack.
     * @return string representation or empty string if the value is null
     */
    public String formatBoolean(@Nullable Boolean value) {
        return datatypeRegistry.getNN(Boolean.class).format(value, uss.getLocale());
    }

    /**
     * Format Integer using {@code integerFormat} string specified in the main message pack.
     * @return string representation or empty string if the value is null
     */
    public String formatInteger(@Nullable Integer value) {
        return datatypeRegistry.getNN(Integer.class).format(value, uss.getLocale());
    }

    /**
     * Format Long using {@code integerFormat} string specified in the main message pack.
     * @return string representation or empty string if the value is null
     */
    public String formatLong(@Nullable Long value) {
        return datatypeRegistry.getNN(Long.class).format(value, uss.getLocale());
    }

    /**
     * Parse Date (date without time) using {@code dateFormat} string specified in the main message pack.
     * @return Date value or null if a blank string is provided
     */
    @Nullable
    public Date parseDate(String str) throws ParseException {
        return datatypeRegistry.getNN(java.sql.Date.class).parse(str, uss.getLocale());
    }

    /**
     * Parse Date (time without date) using {@code timeFormat} string specified in the main message pack.
     * @return Date value or null if a blank string is provided
     */
    @Nullable
    public Date parseTime(String str) throws ParseException {
        return datatypeRegistry.getNN(Time.class).parse(str, uss.getLocale());
    }

    /**
     * Parse Date (date and time) using {@code dateTimeFormat} string specified in the main message pack.
     * <p>Takes into account time zone if it is set for the current user session.</p>
     * @return Date value or null if a blank string is provided
     */
    @Nullable
    public Date parseDateTime(String str) throws ParseException {
        TimeZone timeZone = uss.getUserSession().getTimeZone();
        Datatype<Date> datatype = datatypeRegistry.getNN(Date.class);
        if (datatype instanceof DateTimeDatatype) {
            return ((DateTimeDatatype) datatype).parse(str, uss.getLocale(), timeZone);
        }
        return datatype.parse(str, uss.getLocale());
    }

    /**
     * Parse LocalDate (date without time and without a time-zone) using {@code dateFormat} string specified in the main
     * message pack.
     *
     * @return LocalDate value or null if a blank string is provided
     */
    @Nullable
    public LocalDate parseLocalDate(String str) throws ParseException {
        return datatypeRegistry.getNN(LocalDate.class).parse(str, uss.getLocale());
    }

    /**
     * Parse LocalTime (time without date and without a time-zone) using {@code timeFormat} string specified in the main
     * message pack.
     *
     * @return LocalTime value or null if a blank string is provided
     */
    @Nullable
    public LocalTime parseLocalTime(String str) throws ParseException {
        return datatypeRegistry.getNN(LocalTime.class).parse(str, uss.getLocale());
    }

    /**
     * Parse LocalDateTime (date and time without a time-zone) using {@code dateTimeFormat} string specified in the main
     * message pack.
     *
     * @return LocalDateTime value or null if a blank string is provided
     */
    @Nullable
    public LocalDateTime parseLocalDateTime(String str) throws ParseException {
        return datatypeRegistry.getNN(LocalDateTime.class).parse(str, uss.getLocale());
    }

    /**
     * Parse OffsetDateTime (date and time with an offset from UTC/Greenwich) using {@code offsetDateTimeFormat} string
     * specified in the main message pack.
     *
     * @return OffsetDateTime value or null if a blank string is provided
     */
    @Nullable
    public OffsetDateTime parseOffsetDateTime(String str) throws ParseException {
        return datatypeRegistry.getNN(OffsetDateTime.class).parse(str, uss.getLocale());
    }

    /**
     * Parse OffsetTime (time without date and with an offset from UTC/Greenwich) using {@code offsetTimeFormat} string
     * specified in the main message pack.
     *
     * @return OffsetTime value or null if a blank string is provided
     */
    @Nullable
    public OffsetTime parseOffsetTime(String str) throws ParseException {
        return datatypeRegistry.getNN(OffsetTime.class).parse(str, uss.getLocale());
    }

    /**
     * Parse Double using {@code doubleFormat} string specified in the main message pack.
     * @return Double value or null if a blank string is provided
     */
    @Nullable
    public Double parseDouble(String str) throws ParseException {
        return datatypeRegistry.getNN(Double.class).parse(str, uss.getLocale());
    }

    /**
     * Parse BigDecimal using {@code decimalFormat} string specified in the main message pack.
     * @return BigDecimal value or null if a blank string is provided
     */
    @Nullable
    public BigDecimal parseBigDecimal(String str) throws ParseException {
        return datatypeRegistry.getNN(BigDecimal.class).parse(str, uss.getLocale());
    }

    /**
     * Parse Boolean using {@code trueString} and {@code falseString} strings specified in the main message pack.
     * @return Boolean value or null if a blank string is provided
     */
    @Nullable
    public Boolean parseBoolean(String str) throws ParseException {
        return datatypeRegistry.getNN(Boolean.class).parse(str, uss.getLocale());
    }

    /**
     * Parse Integer using {@code integerFormat} string specified in the main message pack.
     * @return Integer value or null if a blank string is provided
     */
    @Nullable
    public Integer parseInteger(String str) throws ParseException {
        return datatypeRegistry.getNN(Integer.class).parse(str, uss.getLocale());
    }

    /**
     * Parse Long using {@code integerFormat} string specified in the main message pack.
     * @return Long value or null if a blank string is provided
     */
    @Nullable
    public Long parseLong(String str) throws ParseException {
        return datatypeRegistry.getNN(Long.class).parse(str, uss.getLocale());
    }
}