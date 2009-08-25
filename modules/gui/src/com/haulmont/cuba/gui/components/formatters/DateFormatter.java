/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Maksim Tulupov
 * Created: 23.06.2009 16:04:07
 *
 * $Id$
 */
package com.haulmont.cuba.gui.components.formatters;

import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.Formatter;
import org.dom4j.Element;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatter implements Formatter<Date> {

    private Element element;

    public DateFormatter(Element element) {
        this.element = element;
    }

    public String format(Date value) {
        if (value == null) {
            return null;
        }
        final String format = element.attributeValue("format");
        if (format == null) {
            return value.toString();
        } else {
            DateFormat df = new SimpleDateFormat(format);
            return df.format(value);
        }
    }
}
