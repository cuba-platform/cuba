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

public class EmailValidatorTest extends TestCase {

    private Pattern pattern = Pattern.compile(EmailValidator.EMAIL_PATTERN);

    public void testValidate() throws Exception {
        String[] validEmails = {
                "selenium@google.com",
                "a@mail.ru",
                "test@pegas.travel",
                "yuriy.pavlov@r7.com",
                "yuriy-p@mail.ru",
                "test@int64.ru",
                "y_pavlov@mail.ru",
                "abc@hotmail.co.uk",
                "fully-qualified-domain@example.com",
                "zxc@safe-mail.net",
                "asd@o2.co.uk",
                "test@i.ua",
                "example-indeed@strange-example.com",
                "email@t-online.de",
                "abc.xyz@yahoo.com.br",
                "qwe-asd@oi.com.br",
                "my.ema-il@123qwe.co.uk",
                "i.van-petrov23@mail2cuba.com",
                "other.email-with-dash@example.com",
                "i.van-petrov23@mail-2-cuba.com",
                "admin@test.mg.gov.com",
                "admin@tes-t.mg.gov.com",
                "admin@tes-t.mg.g-o-v.com",
                "admin@te-st.m-g.gov.com",
                "admin@t-est.m-g.go-v.com"
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
                "_pavlov@mail.ru",
                "Abc.example.com",
                "just\"not\"right@example.com",
                "_pavlov@-mail.ru",
                "pavlov@mail-.ru",
                "pavlov@-mail-.ru",
                "test@-i.com",
                "test@i-.ru",
                "test@-i-.com",
                "test@i--i.ru",
                ".email@test.com",
                "email.@test.com",
                ".email.@test.com",
                "admin@test-.mg.gov.com",
                "admin@tes-t.-mg.g--ov.com",
                "admin@te-st.m-g.-gov-.com",
                "admin@t-est.m-g-.go-v.com"
        };

        for (String invalidEmail : invalidEmails) {
            Matcher m = pattern.matcher(invalidEmail);
            assertFalse("Valid: " + invalidEmail, m.matches());
        }
    }
}