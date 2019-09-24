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

import com.haulmont.cuba.client.testsupport.CubaClientTestCase;
import com.haulmont.cuba.gui.components.ValidationException;
import org.hibernate.validator.HibernateValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class EmailValidatorTest extends CubaClientTestCase {

    @BeforeEach
    public void setUp() {
        setupInfrastructure();
    }

    @Test
    public void testValidate() {
        String[] validEmails = {
                "",
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
                "admin@t-est.m-g.go-v.com",
                "qwe@mail.com, asd@mail.com",
                "qwe@mail.com; asd@mail.com; zxc@mail.com",
                "qwe@mail.com;asd@mail.com;zxc@mail.com",
                "qwe@mail.com; asd@mail.com, zxc@mail.com",
                "qwe@mail.com; asd@mail.com, zxc@mail.com,",
                "qwe@mail.com; asd@mail.com, zxc@mail.com, ",
        };

        EmailValidator emailValidator = new EmailValidator(messages, createValidator());

        for (String validEmail : validEmails) {
            boolean valid = true;
            try {
                emailValidator.validate(validEmail);
            } catch (ValidationException e) {
                valid = false;
            }
            assertTrue("Should be valid: " + validEmail, valid);
        }
    }

    @Test
    public void testValidateFail() {
        String[] invalidEmails = {
                "@mail.ru",
                "yuriy.@mail.ru",
                "Abc.example.com",
                "just\"not\"right@example.com",
                ".email@test.com",
                "email.@test.com",
                ".email.@test.com",
                "qwe@mail.com, ,zxc@mail.com",
                "qwe@mail.com; ;zxc@mail.com",
                "qwe@mail.com; ;zxc@mail.com,",
                "qwe@mail.com zxc@mail.com",
                "qwe@mail.com zxc@mail.com,"
        };

        EmailValidator emailValidator = new EmailValidator(messages, createValidator());

        for (String invalidEmail : invalidEmails) {
            boolean valid = false;
            try {
                emailValidator.validate(invalidEmail);
                valid = true;
            } catch (ValidationException ignored) {
            }

            assertFalse("Should be not valid: " + invalidEmail, valid);
        }
    }

    protected Validator createValidator() {
        return Validation
                .byProvider(HibernateValidator.class)
                .configure()
                .buildValidatorFactory()
                .getValidator();
    }
}