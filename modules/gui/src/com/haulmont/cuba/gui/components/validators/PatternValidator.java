/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 24.04.2009 9:46:10
 * $Id$
 */
package com.haulmont.cuba.gui.components.validators;

import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.ValidationException;
import com.haulmont.cuba.core.global.MessageUtils;
import org.dom4j.Element;

import java.util.regex.Pattern;

public class PatternValidator implements Field.Validator {

    protected Pattern pattern;
    protected String message;
    protected String messagesPack;

    public PatternValidator(Element element, String messagesPack) {
        this(element.attributeValue("pattern"));
        message = element.attributeValue("message");
        this.messagesPack = messagesPack;
    }

    public PatternValidator(String pattern) {
        this.pattern = Pattern.compile(pattern);
    }

    public void validate(Object value) throws ValidationException {
        if (!(value != null && pattern.matcher(((String) value)).matches())) {
            String msg = message != null ? MessageUtils.loadString(messagesPack, message) : "Invalid value '%s'";
            throw new ValidationException(String.format(msg, value));
        }
    }
}
