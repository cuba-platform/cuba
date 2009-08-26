/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Ilya Grachev
 * Created: 26.08.2009 12:29:31
 *
 * $Id$
 */
package com.haulmont.cuba.gui.components.formatters;

import org.dom4j.Element;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;

import com.haulmont.cuba.gui.components.Formatter;

public class BigDecimalFormatter implements Formatter<BigDecimal> {
    private Element element;

    public BigDecimalFormatter(Element element) {
        this.element = element;
    }

    public String format(BigDecimal value) {
        if (value == null) {
            return null;
        }
        final String format = element.attributeValue("format");
        if (format == null) {
            return value.toString();
        } else {
            DecimalFormat df = new DecimalFormat(format);
            return df.format(value);
        }
    }
}
