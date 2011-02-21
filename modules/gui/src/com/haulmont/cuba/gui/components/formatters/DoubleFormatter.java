/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Evgeny Zaharchenko
 * Created: 19.02.11 18:59
 *
 * $Id$
 */

package com.haulmont.cuba.gui.components.formatters;

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.components.Formatter;
import org.dom4j.Element;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class DoubleFormatter implements Formatter<Double> {
    private Element element;

    public DoubleFormatter(Element element) {
        this.element = element;
    }

    public String format(Double value) {
        if (value == null) {
            return null;
        }
        String format = element.attributeValue("format");
        if (format == null) {
            return value.toString();
        } else {
            if (format.startsWith("msg://")) {
                format = MessageProvider.getMessage(
                        AppConfig.getInstance().getMessagesPack(), format.substring(6, format.length()));
            }
            DecimalFormat df = new DecimalFormat(format);
            return df.format(new BigDecimal(value));
        }
    }
}
