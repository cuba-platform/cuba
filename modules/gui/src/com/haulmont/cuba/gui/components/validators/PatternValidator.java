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
import com.haulmont.cuba.gui.MessageUtils;
import org.dom4j.Element;

import java.util.regex.Pattern;

public class PatternValidator implements Field.Validator {
    protected Pattern pattern;
    protected String message;

    public PatternValidator(Element element) {
        this(element.attributeValue("pattern"));
        message = element.attributeValue("message");
    }

    public PatternValidator(String pattern) {
        this.pattern = Pattern.compile(pattern);
    }

    public boolean isValid(Object value) {
        return pattern.matcher(((String) value)).matches();
    }

    public void validate(Object value) throws ValidationException {
        if (!isValid(value)) {
            String msg = message != null ? MessageUtils.loadString(message) : "Invalid value '%s'";
            throw new ValidationException(String.format(msg, value));
        }
    }
}
