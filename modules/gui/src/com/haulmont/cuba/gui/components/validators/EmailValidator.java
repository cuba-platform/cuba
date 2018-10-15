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

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.BeanValidation;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.ValidationException;
import org.dom4j.Element;
import org.hibernate.validator.constraints.Email;

import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;

public class EmailValidator implements Field.Validator {

    protected String message;
    protected String messagesPack;
    protected Messages messages;

    protected Validator validator;

    public EmailValidator() {
        messages = AppBeans.get(Messages.class);
        validator = AppBeans.get(BeanValidation.class)
                .getValidator();
    }

    public EmailValidator(Element element, String messagesPack) {
        this();
        message = element.attributeValue("message");
        this.messagesPack = messagesPack;
    }

    /**
     * INTERNAL. Used in tests.
     */
    protected EmailValidator(Messages messages, Validator validator) {
        this.messages = messages;
        this.validator = validator;
    }

    @Override
    public void validate(Object value) throws ValidationException {
        if (value == null) {
            return;
        }

        List<String> emails = collectEmails((String) value);
        if (emails.isEmpty()) {
            return;
        }

        for (String email : emails) {
            boolean valid = validator.validateValue(EmailValidationPojo.class, "email", email).isEmpty();
            if (!valid) {
                String msg = message != null ?
                        messages.getTools().loadString(messagesPack, message)
                        : null;

                if (msg == null) {
                    msg = messages.getMainMessage("validation.invalidEmail");
                }

                throw new ValidationException(String.format(msg, value));
            }
        }
    }

    protected List<String> collectEmails(String emailString) {
        List<String> emails = new ArrayList<>();

        int sepIdx = getSepIdx(emailString);
        while (sepIdx > 0) {
            String email = emailString.substring(0, sepIdx).trim();
            emails.add(preventEmpty(email));

            emailString = emailString.substring(sepIdx + 1);
            sepIdx = getSepIdx(emailString);
        }

        emails.add(emailString.trim());

        return emails;
    }

    protected String preventEmpty(String s) {
        // make validator fall on blank emails for rejecting trailing separators
        return s.isEmpty() ? " " : s;
    }

    protected int getSepIdx(String emailString) {
        int semicolonIdx = emailString.indexOf(';');
        int commaIdx = emailString.indexOf(',');

        if (semicolonIdx < 0 && commaIdx < 0) {
            return -1;
        }
        if (semicolonIdx >= 0 && commaIdx >= 0) {
            return Math.min(semicolonIdx, commaIdx);
        }

        return semicolonIdx != -1 ? semicolonIdx : commaIdx;
    }

    protected static class EmailValidationPojo {

        @Email
        private final String email;

        public EmailValidationPojo(String email) {
            this.email = email;
        }

        public String getEmail() {
            return email;
        }
    }
}