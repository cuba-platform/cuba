/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.validators;

import junit.framework.TestCase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author artamonov
 * @version $Id$
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