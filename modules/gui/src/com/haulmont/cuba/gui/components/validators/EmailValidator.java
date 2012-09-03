/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 24.04.2009 9:43:40
 * $Id$
 */
package com.haulmont.cuba.gui.components.validators;

import org.dom4j.Element;

public class EmailValidator extends PatternValidator {

    static private String sDomen = "[a-zA-Z[0-9]][a-zA-Z[0-9]\u005F\u002E\u002D]*[a-z||A-Z||0-9]";
    static private String sDomen2 = "([a-zA-Z]){2,4}";
    public static String EMAIL_PATTERN = sDomen + "@" + sDomen + "\u002E" + sDomen2;
    public static String MULTI_EMAIL_PATTERN = EMAIL_PATTERN + "([;,]+[\\s]*" + EMAIL_PATTERN + ")*";

    public EmailValidator(Element element, String messagesPack) {
        super(MULTI_EMAIL_PATTERN);
        message = element.attributeValue("message");
        this.messagesPack = messagesPack;
    }

    public EmailValidator() {
        super(MULTI_EMAIL_PATTERN);
    }
}
