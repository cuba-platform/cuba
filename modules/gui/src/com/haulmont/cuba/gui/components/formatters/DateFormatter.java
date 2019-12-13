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
package com.haulmont.cuba.gui.components.formatters;

import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.datatypes.FormatStrings;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.LocaleResolver;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Function;

/**
 * {@link Date} formatter to be used in screen descriptors.
 * <br> Either {@code format} or {@code type} attributes should be defined in the {@code formatter} element.
 * <ul>
 *     <li> {@code format} - format string for {@code SimpleDateFormat}</li>
 *     <li> {@code type} - {@code DATE} or {@code DATETIME} - if specified, the value will be formatted
 *     by means of {@code DateDatatype} or {@code DateTimeDatatype} respectively.</li>
 * </ul>
 * <br> Example usage:
 * <pre>
 * &lt;formatter class=&quot;com.haulmont.cuba.gui.components.formatters.DateFormatter&quot; format=&quot;msg://dateFormat&quot;
 * </pre>
 */
public class DateFormatter implements Function<Date, String> {

    private Element element;

    protected UserSessionSource userSessionSource = AppBeans.get(UserSessionSource.NAME);
    protected Messages messages = AppBeans.get(Messages.NAME);

    public DateFormatter(Element element) {
        this.element = element;
    }

    @Override
    public String apply(Date value) {
        if (value == null) {
            return null;
        }
        String format = element.attributeValue("format");
        if (StringUtils.isBlank(format)) {
            String type = element.attributeValue("type");
            if (type != null) {
                FormatStrings formatStrings = Datatypes.getFormatStrings(userSessionSource.getLocale());
                if (formatStrings == null)
                    throw new IllegalStateException("FormatStrings are not defined for " +
                            LocaleResolver.localeToString(userSessionSource.getLocale()));
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

            if (Boolean.parseBoolean(element.attributeValue("useUserTimezone"))) {
                UserSessionSource userSessionSource = AppBeans.get(UserSessionSource.NAME);
                if (userSessionSource.checkCurrentUserSession()) {
                    UserSession userSession = userSessionSource.getUserSession();
                    if (userSession.getTimeZone() != null) {
                        df.setTimeZone(userSession.getTimeZone());
                    }
                }
            }

            return df.format(value);
        }
    }
}