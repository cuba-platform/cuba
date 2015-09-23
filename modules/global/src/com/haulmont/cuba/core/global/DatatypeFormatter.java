/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.global;

import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.datatypes.impl.*;

import javax.annotation.ManagedBean;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.TimeZone;

/**
 * Convenience bean for locale-dependent conversion of some widely used data types to and from strings.
 * <p>
 * For locale-indepenedent conversion use {@link com.haulmont.chile.core.datatypes.Datatype} methods directly.
 *
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean(DatatypeFormatter.NAME)
public class DatatypeFormatter {

    public static final String NAME = "cuba_DatatypeFormatter";

    @Inject
    protected UserSessionSource uss;

    @Inject
    protected TimeZones timeZones;

    /**
     * Format Date (date without time) using {@code dateFormat} string specified in the main message pack.
     * @return string representation or empty string if the value is null
     */
    public String formatDate(@Nullable Date value) {
        DateDatatype datatype = (DateDatatype) Datatypes.get(DateDatatype.NAME);
        return datatype.format(value, uss.getLocale());
    }

    /**
     * Format Date (time without date) using {@code timeFormat} string specified in the main message pack.
     * @return string representation or empty string if the value is null
     */
    public String formatTime(@Nullable Date value) {
        TimeDatatype datatype = (TimeDatatype) Datatypes.get(TimeDatatype.NAME);
        return datatype.format(value, uss.getLocale());
    }

    /**
     * Format Date (date and time) using {@code dateTimeFormat} string specified in the main message pack.
     * <p>Takes into account time zone if it is set for the current user session.</p>
     * @return string representation or empty string if the value is null
     */
    public String formatDateTime(@Nullable Date value) {
        TimeZone tz = uss.getUserSession().getTimeZone();
        if (tz != null)
            value = timeZones.convert(value, TimeZone.getDefault(), tz);

        DateTimeDatatype datatype = (DateTimeDatatype) Datatypes.get(DateTimeDatatype.NAME);
        return datatype.format(value, uss.getLocale());
    }

    /**
     * Format Double using {@code doubleFormat} string specified in the main message pack.
     * @return string representation or empty string if the value is null
     */
    public String formatDouble(@Nullable Double value) {
        DoubleDatatype datatype = (DoubleDatatype) Datatypes.get(DoubleDatatype.NAME);
        return datatype.format(value, uss.getLocale());
    }

    /**
     * Format BigDecimal using {@code decimalFormat} string specified in the main message pack.
     * @return string representation or empty string if the value is null
     */
    public String formatBigDecimal(@Nullable BigDecimal value) {
        BigDecimalDatatype datatype = (BigDecimalDatatype) Datatypes.get(BigDecimalDatatype.NAME);
        return datatype.format(value, uss.getLocale());
    }

    /**
     * Format Boolean using {@code trueString} and {@code falseString} strings specified in the main message pack.
     * @return string representation or empty string if the value is null
     */
    public String formatBoolean(@Nullable Boolean value) {
        BooleanDatatype datatype = (BooleanDatatype) Datatypes.get(BooleanDatatype.NAME);
        return datatype.format(value, uss.getLocale());
    }

    /**
     * Format Integer using {@code integerFormat} string specified in the main message pack.
     * @return string representation or empty string if the value is null
     */
    public String formatInteger(@Nullable Integer value) {
        IntegerDatatype datatype = (IntegerDatatype) Datatypes.get(IntegerDatatype.NAME);
        return datatype.format(value, uss.getLocale());
    }

    /**
     * Format Long using {@code integerFormat} string specified in the main message pack.
     * @return string representation or empty string if the value is null
     */
    public String formatLong(@Nullable Long value) {
        LongDatatype datatype = (LongDatatype) Datatypes.get(LongDatatype.NAME);
        return datatype.format(value, uss.getLocale());
    }

    /**
     * Parse Date (date without time) using {@code dateFormat} string specified in the main message pack.
     * @return Date value or null if a blank string is provided
     */
    @Nullable
    public Date parseDate(String str) throws ParseException {
        DateDatatype datatype = (DateDatatype) Datatypes.get(DateDatatype.NAME);
        return datatype.parse(str, uss.getLocale());
    }

    /**
     * Parse Date (time without date) using {@code timeFormat} string specified in the main message pack.
     * @return Date value or null if a blank string is provided
     */
    @Nullable
    public Date parseTime(String str) throws ParseException {
        TimeDatatype datatype = (TimeDatatype) Datatypes.get(TimeDatatype.NAME);
        return datatype.parse(str, uss.getLocale());
    }

    /**
     * Parse Date (date and time) using {@code dateTimeFormat} string specified in the main message pack.
     * <p>Takes into account time zone if it is set for the current user session.</p>
     * @return Date value or null if a blank string is provided
     */
    @Nullable
    public Date parseDateTime(String str) throws ParseException {
        DateTimeDatatype datatype = (DateTimeDatatype) Datatypes.get(DateTimeDatatype.NAME);
        Date date = datatype.parse(str, uss.getLocale());

        TimeZone tz = uss.getUserSession().getTimeZone();
        if (tz != null)
            date = timeZones.convert(date, tz, TimeZone.getDefault());

        return date;
    }

    /**
     * Parse Double using {@code doubleFormat} string specified in the main message pack.
     * @return Double value or null if a blank string is provided
     */
    @Nullable
    public Double parseDouble(String str) throws ParseException {
        DoubleDatatype datatype = (DoubleDatatype) Datatypes.get(DoubleDatatype.NAME);
        return datatype.parse(str, uss.getLocale());
    }

    /**
     * Parse BigDecimal using {@code decimalFormat} string specified in the main message pack.
     * @return BigDecimal value or null if a blank string is provided
     */
    @Nullable
    public BigDecimal parseBigDecimal(String str) throws ParseException {
        BigDecimalDatatype datatype = (BigDecimalDatatype) Datatypes.get(BigDecimalDatatype.NAME);
        return datatype.parse(str, uss.getLocale());
    }

    /**
     * Parse Boolean using {@code trueString} and {@code falseString} strings specified in the main message pack.
     * @return Boolean value or null if a blank string is provided
     */
    @Nullable
    public Boolean parseBoolean(String str) throws ParseException {
        BooleanDatatype datatype = (BooleanDatatype) Datatypes.get(BooleanDatatype.NAME);
        return datatype.parse(str, uss.getLocale());
    }

    /**
     * Parse Integer using {@code integerFormat} string specified in the main message pack.
     * @return Integer value or null if a blank string is provided
     */
    @Nullable
    public Integer parseInteger(String str) throws ParseException {
        IntegerDatatype datatype = (IntegerDatatype) Datatypes.get(IntegerDatatype.NAME);
        return datatype.parse(str, uss.getLocale());
    }

    /**
     * Parse Long using {@code integerFormat} string specified in the main message pack.
     * @return Long value or null if a blank string is provided
     */
    @Nullable
    public Long parseLong(String str) throws ParseException {
        LongDatatype datatype = (LongDatatype) Datatypes.get(LongDatatype.NAME);
        return datatype.parse(str, uss.getLocale());
    }
}
