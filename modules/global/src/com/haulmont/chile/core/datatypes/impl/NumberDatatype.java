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

package com.haulmont.chile.core.datatypes.impl;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.text.*;

/**
 * <p>$Id: NumberDatatype.java 4904 2011-05-31 09:45:58Z krivopustov $</p>
 *
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
