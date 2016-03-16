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

import junit.framework.TestCase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 */
public class EmailValidatorTest extends TestCase {

    private Pattern pattern = Pattern.compile(EmailValidator.EMAIL_PATTERN);

    public void testValidate() throws Exception {
        String[] validEmails = {
                "selenium@google.com",
                "a@mail.ru",
                "test@pegas.travel",
                "yuriy.pavlov@mail.ru",
                "yuriy-p@mail.ru",
                "test@int64.ru",
                "y_pavlov@mail.ru"
        };

        for (String validEmail : validEmails) {
            assertTrue("Invalid: " + validEmail, pattern.matcher(validEmail).matches());
        }
    }

    public void testValidateFail() throws Exception {
        String[] invalidEmails = {
                "selenium#@google.com",
                "@mail.ru",
                "test@pegas.travelersessolong",
                "yuriy.@mail.ru",
                "yuriy-@mail.ru",
                "_pavlov@mail.ru"
        };

        for (String invalidEmail : invalidEmails) {
            Matcher m = pattern.matcher(invalidEmail);
            assertFalse("Valid: " + invalidEmail, m.matches());
        }
    }
}