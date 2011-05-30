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

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.datatypes.FormatStrings;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.core.global.UserSessionProvider;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.components.Formatter;
import org.dom4j.Element;

import java.text.DecimalFormat;

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
            Datatype datatype = Datatypes.get(value.getClass());
            if (datatype == null)
                throw new IllegalArgumentException("No datatype for " + value.getClass());

            return datatype.format(value, UserSessionProvider.getLocale());
        } else {
            if (pattern.startsWith("msg://")) {
                pattern = MessageProvider.getMessage(
                        AppConfig.getMessagesPack(), pattern.substring(6, pattern.length()));
            }
            FormatStrings formatStrings = Datatypes.getFormatStrings(UserSessionProvider.getLocale());
            DecimalFormat format = new DecimalFormat(pattern, formatStrings.getFormatSymbols());
            return format.format(value);
        }
    }
}
