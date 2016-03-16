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

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.datatypes.FormatStrings;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.components.Formatter;
import org.dom4j.Element;

import java.text.DecimalFormat;

/**
 * Number formatter to be used in screen descriptors and controllers.
 * <p/> If defined in XML together with <code>format</code> attribute, uses this format, otherwise formats by means of
 * {@link Datatype#format(Object, java.util.Locale)}.
 *
 */
public class NumberFormatter implements Formatter<Number> {

    private Element element;

    protected UserSessionSource userSessionSource = AppBeans.get(UserSessionSource.NAME);
    protected Messages messages = AppBeans.get(Messages.NAME);

    public NumberFormatter() {
    }

    public NumberFormatter(Element element) {
        this.element = element;
    }

    @Override
    public String format(Number value) {
        if (value == null) {
            return null;
        }
        String pattern = element != null ? element.attributeValue("format") : null;

        if (pattern == null) {
            Datatype datatype = Datatypes.getNN(value.getClass());
            return datatype.format(value, userSessionSource.getLocale());
        } else {
            if (pattern.startsWith("msg://")) {
                pattern = messages.getMainMessage(pattern.substring(6, pattern.length()));
            }
            FormatStrings formatStrings = Datatypes.getFormatStrings(userSessionSource.getLocale());
            if (formatStrings == null)
                throw new IllegalStateException("FormatStrings are not defined for " + userSessionSource.getLocale());
            DecimalFormat format = new DecimalFormat(pattern, formatStrings.getFormatSymbols());
            return format.format(value);
        }
    }
}