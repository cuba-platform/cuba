/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components.formatters;

import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.datatypes.FormatStrings;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.components.Formatter;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * {@link Date} formatter to be used in screen descriptors.
 * <p/> Either <code>format</code> or <code>type</code> attributes should be defined in the <code>formatter</code> element.
 * <ul>
 *     <li/> <code>format</code> - format string for <code>SimpleDateFormat</code>
 *     <li/> <code>type</code> - <code>DATE</code> or <code>DATETIME</code> - if specified, the value will be formatted
 *     by means of {@code DateDatatype} or {@code DateTimeDatatype} respectively.
 * </ul>
 * <p/> Example usage:
 * <pre>
 * &lt;formatter class=&quot;com.haulmont.cuba.gui.components.formatters.DateFormatter&quot; format=&quot;msg://dateFormat&quot;
 * </pre>
 *
 * @author tulupov
 * @version $Id$
 */
public class DateFormatter implements Formatter<Date> {

    private Element element;

    protected UserSessionSource userSessionSource = AppBeans.get(UserSessionSource.NAME);
    protected Messages messages = AppBeans.get(Messages.NAME);

    public DateFormatter(Element element) {
        this.element = element;
    }

    @Override
    public String format(Date value) {
        if (value == null) {
            return null;
        }
        String format = element.attributeValue("format");
        if (StringUtils.isBlank(format)) {
            String type = element.attributeValue("type");
            if (type != null) {
                FormatStrings formatStrings = Datatypes.getFormatStrings(userSessionSource.getLocale());
                if (formatStrings == null)
                    throw new IllegalStateException("FormatStrings are not defined for " + userSessionSource.getLocale());
                switch (type) {
                    case "DATE":
                        format = formatStrings.getDateFormat();
                        break;
                    case "DATETIME":
                        format = formatStrings.getDateTimeFormat();
                        break;
                    default:
                        throw new RuntimeException("Illegal formatter type value");
                }
            }
        }

        if (StringUtils.isBlank(format)) {
            return value.toString();
        } else {
            if (format.startsWith("msg://")) {
                format = messages.getMainMessage(format.substring(6, format.length()));
            }
            DateFormat df = new SimpleDateFormat(format);
            return df.format(value);
        }
    }
}
