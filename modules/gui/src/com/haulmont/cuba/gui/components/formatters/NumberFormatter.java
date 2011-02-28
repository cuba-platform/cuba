/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 *
 * Author: Nikolay Gorodnov
 * Created: 22.02.2011 16:27:40
 *
 * $Id$
 */
package com.haulmont.cuba.gui.components.formatters;

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.core.global.MessageUtils;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.components.Formatter;
import org.dom4j.Element;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class NumberFormatter implements Formatter<Number> {
    private Element element;

    private static final long serialVersionUID = -7507422168280540490L;

    public NumberFormatter() {
    }

    public NumberFormatter(Element element) {
        this.element = element;
    }

    public String format(Number value) {
        if (value == null) {
            return null;
        }
        String pattern = element != null
                ? element.attributeValue("format") : null;
        if (pattern == null) {
            if (value instanceof Integer ||
                    value instanceof Short ||
                    value instanceof Byte ||
                    value instanceof AtomicInteger ||
                    value instanceof BigInteger
                    ) {
                pattern = MessageUtils.getIntegerFormat();
            } else if (value instanceof Long || value instanceof AtomicLong) {
                pattern = MessageUtils.getLongFormat();
            } else if (value instanceof BigDecimal) {
                pattern = MessageUtils.getBigDecimalFormat();
            } else {
                pattern = MessageUtils.getDoubleFormat();
            }
        } else {
            if (pattern.startsWith("msg://")) {
                pattern = MessageProvider.getMessage(
                        AppConfig.getInstance().getMessagesPack(), pattern.substring(6, pattern.length()));
            }
        }

        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(MessageUtils.getNumberDecimalSeparator());
        symbols.setGroupingSeparator(MessageUtils.getNumberGroupingSeparator());

        DecimalFormat format = new DecimalFormat(pattern, symbols);
        return format.format(value);
    }
}
