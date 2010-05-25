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

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.components.Formatter;
import com.haulmont.cuba.gui.components.Table;
import org.apache.commons.lang.StringUtils;
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
        String format;
        String type = element.attributeValue("type");
        if (type != null) {
            Table.Column.FormatterType ftype = Table.Column.FormatterType.valueOf(type);
            switch (ftype) {
                case DATE:
                    format = MessageProvider.getMessage(AppConfig.getInstance().getMessagesPack(), "dateFormat");
                    break;
                case DATETIME:
                    format = MessageProvider.getMessage(AppConfig.getInstance().getMessagesPack(), "dateTimeFormat");
                    break;
                default:
                    throw new RuntimeException("Illegal formatter type value");
            }
        }
        else {
            format = element.attributeValue("format");
        }

        if (StringUtils.isEmpty(format)) {
            return value.toString();
        }
        else {
            if (format.startsWith("msg://")) {
                format = MessageProvider.getMessage(
                        AppConfig.getInstance().getMessagesPack(), format.substring(6, format.length()));
            }
            DateFormat df = new SimpleDateFormat(format);
            return df.format(value);
        }
    }
}
