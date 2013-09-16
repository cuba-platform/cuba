/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components.validators;

import org.dom4j.Element;

/**
 * @author abramov
 * @version $Id$
 */
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