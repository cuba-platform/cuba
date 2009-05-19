/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Maksim Tulupov
 * Created: 06.05.2009 15:00:15
 *
 * $Id$
 */
package com.haulmont.cuba.gui.components.validators;

import com.haulmont.cuba.gui.MessageUtils;
import com.haulmont.cuba.gui.components.ValidationException;
import com.haulmont.cuba.gui.components.Field;
import org.dom4j.Element;

public class DoubleValidator implements Field.Validator{

    protected String message;

    public DoubleValidator(Element element) {
        message = element.attributeValue("message");
    }

    public boolean isValid(Object value) {
        if (value instanceof String) {
            try {
                Double d = Double.valueOf((String) value);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        } else if (value instanceof Double) {
            return true;
        } else {
            return false;
        }
    }

    public void validate(Object value) throws ValidationException {
        if (!isValid(value)) {
            String msg = message != null ? MessageUtils.loadString(message) : "Invalid value '%s'";
            throw new ValidationException(String.format(msg, value));
        }
    }
}
