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
package com.haulmont.cuba.gui.components.validators;

import com.haulmont.cuba.gui.components.ValidationException;
import org.dom4j.Element;

public class EmailValidator extends PatternValidator {

    private static String EMAIL_NAME = "([a-zA-Z[0-9]][a-zA-Z[0-9]\u005F\u002E\u002D]*[a-z||A-Z||0-9]|[a-zA-Z[0-9]])";
    private static String EMAIL_DOMAIN = "([a-zA-Z[0-9]][a-zA-Z[0-9]\u005F\u002E\u002D]*[a-z||A-Z||0-9])";
    private static String EMAIL_DOMAIN_ZONE = "([a-zA-Z[0-9]]{2,8})";

    public static final String EMAIL_PATTERN = EMAIL_NAME + "@" + EMAIL_DOMAIN + "\\." + EMAIL_DOMAIN_ZONE;
    public static final String MULTI_EMAIL_PATTERN = EMAIL_PATTERN + "([;,]+[\\s]*" + EMAIL_PATTERN + ")*";

    public EmailValidator(Element element, String messagesPack) {
        super(MULTI_EMAIL_PATTERN);
        message = element.attributeValue("message");
        this.messagesPack = messagesPack;
    }

    public EmailValidator() {
        super(MULTI_EMAIL_PATTERN);
    }

    @Override
    public void validate(Object value) throws ValidationException {
        if (value != null)
            super.validate(value);
    }
}