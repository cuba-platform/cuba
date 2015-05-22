/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components.validators;

import com.haulmont.cuba.gui.components.ValidationException;
import org.dom4j.Element;

/**
 * @author abramov
 * @version $Id$
 */
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