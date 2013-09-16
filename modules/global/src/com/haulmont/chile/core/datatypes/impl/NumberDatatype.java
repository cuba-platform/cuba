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

    protected NumberFormat format;

    protected NumberDatatype(Element element) {
        final String pattern = element.attributeValue("format");
        if (pattern != null) {
            final DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols();

            String decimalSeparator = element.attributeValue("decimalSeparator");
            if (!StringUtils.isBlank(decimalSeparator))
                formatSymbols.setDecimalSeparator(decimalSeparator.charAt(0));

            String groupingSeparator = element.attributeValue("groupingSeparator");
            if (!StringUtils.isBlank(groupingSeparator))
                formatSymbols.setGroupingSeparator(groupingSeparator.charAt(0));


            format = new DecimalFormat(pattern, formatSymbols);
        } else {
            format = NumberFormat.getNumberInstance();
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
