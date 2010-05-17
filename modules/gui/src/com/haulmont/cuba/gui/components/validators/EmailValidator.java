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

    static private String sDomen = "[a-z][a-z[0-9]\u005F\u002E\u002D]*[a-z||0-9]";
    static private String sDomen2 = "([a-z]){2,4}";

    public EmailValidator(Element element, String messagesPack) {
        super(sDomen + "@" + sDomen + "\u002E" + sDomen2);
        message = element.attributeValue("message");
        this.messagesPack = messagesPack;
    }

    public EmailValidator() {
        super(sDomen + "@" + sDomen + "\u002E" + sDomen2);
    }
}
