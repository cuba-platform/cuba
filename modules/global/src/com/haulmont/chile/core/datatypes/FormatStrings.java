/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.chile.core.datatypes;

import java.text.DecimalFormatSymbols;

/**
 * Localized format strings container.
 * <p/> An instance of this class can be acquired through {@link Datatypes#getFormatStrings(java.util.Locale)}.
 *
 * @author krivopustov
 * @version $Id$
 */
public class FormatStrings {

    private DecimalFormatSymbols formatSymbols;
    private String integerFormat;
    private String doubleFormat;
    private String decimalFormat;
    private String dateFormat;
    private String dateTimeFormat;
    private String timeFormat;
    private String trueString;
    private String falseString;

    public FormatStrings(char decimalSeparator, char groupingSeparator,
                         String integerFormat, String doubleFormat, String decimalFormat,
                         String dateFormat, String dateTimeFormat,
                         String timeFormat, String trueString, String falseString) {
        formatSymbols = new DecimalFormatSymbols();
        formatSymbols.setDecimalSeparator(decimalSeparator);
        formatSymbols.setGroupingSeparator(groupingSeparator);
        this.integerFormat = integerFormat;
        this.doubleFormat = doubleFormat;
        this.decimalFormat = decimalFormat;
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

    public String getDecimalFormat() {
        return decimalFormat;
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
