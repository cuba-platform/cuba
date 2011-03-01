/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 *
 * Author: Nikolay Gorodnov
 * Created: 01.03.2011 11:04:28
 *
 * $Id$
 */
package com.haulmont.cuba.gui.components.validators;

import com.haulmont.cuba.core.global.MessageUtils;

import java.text.*;
import java.util.Date;

public abstract class ValidationHelper {
    public static Number parseNumber(String value, String pattern) throws ParseException {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(MessageUtils.getNumberDecimalSeparator());
        symbols.setGroupingSeparator(MessageUtils.getNumberGroupingSeparator());

        if (symbols.getGroupingSeparator() == '\u00a0' || symbols.getGroupingSeparator() == '\u0020') {
            value = value.replace(" ", "");
        }

        DecimalFormat format = new DecimalFormat(pattern, symbols);

        ParsePosition pos = new ParsePosition(0);
        Number result = format.parse(value, pos);
        if (pos.getIndex() != value.length()) {
            throw new ParseException(
                    String.format("Unparseable number: \"%s\"", value),
                    pos.getErrorIndex()
            );
        }
        return result;
    }

    public static Date parseDate(String value) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(MessageUtils.getDateFormat());
        ParsePosition pos = new ParsePosition(0);
        Date result = format.parse(value, pos);
        if (pos.getIndex() != value.length()) {
            throw new ParseException(
                    String.format("Unparseable value: \"%s\"", value),
                    pos.getErrorIndex()
            );
        }
        return result;
    }

    public static Date parseDateTime(String value) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(MessageUtils.getDateTimeFormat());
        ParsePosition pos = new ParsePosition(0);
        Date result = format.parse(value, pos);
        if (pos.getIndex() != value.length()) {
            throw new ParseException(
                    String.format("Unparseable value: \"%s\"", value),
                    pos.getErrorIndex()
            );
        }
        return result;
    }
}
