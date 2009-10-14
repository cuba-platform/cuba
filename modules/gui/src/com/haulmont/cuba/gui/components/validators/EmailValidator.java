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

    public EmailValidator(Element element, String messagesPack) {
        super(".+@.+\\.[a-z]+");
        message = element.attributeValue("message");
        this.messagesPack = messagesPack;
    }

    public EmailValidator() {
        super(".+@.+\\.[a-z]+");
    }
}
