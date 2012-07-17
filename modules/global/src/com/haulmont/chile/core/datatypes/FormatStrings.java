/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.chile.core.datatypes;

import java.text.DecimalFormatSymbols;

/**
 * Localized format strings container.
 * An instance of this object can be acquired through {@link Datatypes#getFormatStrings(java.util.Locale)}
 *
 * <p>$Id: FormatStrings.java 5816 2011-09-05 07:21:39Z devyatkin $</p>
 *
 * @author krivopustov
 */
public class FormatStrings {
    private DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols();
    private String integerFormat;
    private String doubleFormat;
    private String dateFormat;
    private String dateTimeFormat;
    private String timeFormat;
    private String trueString;
    private String falseString;

    public FormatStrings(char decimalSeparator, char groupingSeparator,
                         String integerFormat, String doubleFormat,
                         String dateFormat, String dateTimeFormat,
                         String timeFormat, String trueString, String falseString)
    {
        formatSymbols = new DecimalFormatSymbols();
        formatSymbols.setDecimalSeparator(decimalSeparator);
        formatSymbols.setGroupingSeparator(groupingSeparator);
        this.integerFormat = integerFormat;
        this.doubleFormat = doubleFormat;
        this.dateFormat = dateFormat;
        this.dateTimeFormat = dateTimeFormat;
        this.timeFormat = timeFormat;
        this.trueString = trueString;
        this.falseString = falseString;
    }

    public DecimalFormatSymbols getFormatSymbols() {
        return formatSymbols;
    }

    public String getIntegerFormat() {
        return integerFormat;
    }

    public String getDoubleFormat() {
        return doubleFormat;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public String getDateTimeFormat() {
        return dateTimeFormat;
    }

    public String getTimeFormat() {
        return timeFormat;
    }

    public String getTrueString() {
        return trueString;
    }

    public String getFalseString() {
        return falseString;
    }
}
