/*
 * Copyright (c) 2008-2017 Haulmont.
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
 */

package com.haulmont.cuba.gui.app.core.categories;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.ValidationException;
import org.dom4j.Element;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SizeWithUnitValidator implements Field.Validator {
    public static final String SIZE_PATTERN = "^(-?\\d+(?:\\.\\d+)?)(%|px)?$";
    public static final int MAX_SIZE_PERCENTS = 100;
    public static final int MAX_SIZE_PIXELS = 1920;

    protected String messagesPack;
    protected String message;

    public SizeWithUnitValidator(Element element, String messagesPack) {
        this.message = element.attributeValue("message");
        this.messagesPack = messagesPack;
    }

    public String getMessagesPack() {
        return messagesPack;
    }

    public void setMessagesPack(String messagesPack) {
        this.messagesPack = messagesPack;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void validate(Object value) throws ValidationException {
        if (value == null) {
            return;
        }

        MessageTools messages = AppBeans.get(MessageTools.NAME);

        if (!(value instanceof String)) {
            throw new ValidationException(messages.loadString(messagesPack, message));
        }

        String s = (String) value;

        s = s.trim();
        if ("".equals(value)) {
            return;
        }

        Matcher matcher = Pattern.compile(SIZE_PATTERN).matcher(s);
        if (!matcher.find()) {
            throw new ValidationException(messages.loadString(messagesPack, message));
        }

        double size = Double.parseDouble(matcher.group(1));

        String symbol = matcher.group(2);
        if ("%".equals(symbol)) {
            if (size < 1 || size > MAX_SIZE_PERCENTS) {
                throw new ValidationException(messages.loadString(messagesPack, message));
            }
        } else if ("px".equals(symbol) || symbol == null) {
            if (size < 1 || size > MAX_SIZE_PIXELS) {
                throw new ValidationException(messages.loadString(messagesPack, message));
            }
        }
    }
}