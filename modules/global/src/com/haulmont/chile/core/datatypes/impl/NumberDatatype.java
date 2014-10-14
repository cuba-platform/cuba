/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.chile.core.datatypes.impl;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.text.*;

/**
 * <p>$Id: NumberDatatype.java 4904 2011-05-31 09:45:58Z krivopustov $</p>
 *
 * @author krivopustov
 */
public abstract class NumberDatatype {

    protected String formatPattern;
    protected String decimalSeparator;
    protected String groupingSeparator;

    protected NumberDatatype(Element element) {
        formatPattern = element.attributeValue("format");
        decimalSeparator = element.attributeValue("decimalSeparator");
        groupingSeparator = element.attributeValue("groupingSeparator");
    }

    protected NumberFormat createFormat() {
        if (formatPattern != null) {
            DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols();

            if (!StringUtils.isBlank(decimalSeparator))
                formatSymbols.setDecimalSeparator(decimalSeparator.charAt(0));

            if (!StringUtils.isBlank(groupingSeparator))
                formatSymbols.setGroupingSeparator(groupingSeparator.charAt(0));

            return new DecimalFormat(formatPattern, formatSymbols);
        } else {
            return NumberFormat.getNumberInstance();
        }
    }

    protected Number parse(String value, NumberFormat format) throws ParseException {
        ParsePosition pos = new ParsePosition(0);
        Number res = format.parse(value.trim(), pos);
        if (pos.getIndex() != value.length()) {
            throw new ParseException(
                    String.format("Unparseable number: \"%s\"", value),
                    pos.getErrorIndex()
            );
        }
        return res;
    }
}
