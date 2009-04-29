/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Maksim Tulupov
 * Created: 29.04.2009 13:01:39
 *
 * $Id$
 */
package com.haulmont.cuba.gui.components.validators;

import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.ValidationException;
import com.haulmont.cuba.gui.MessageUtils;
import org.dom4j.Element;
import org.apache.commons.lang.ObjectUtils;

public class IntegerValidator implements Field.Validator {

    protected String message;
    protected String onlyPositive;

    public IntegerValidator(Element element) {
        message = element.attributeValue("message");
        onlyPositive = element.attributeValue("onlyPositive");
    }

    public boolean isValid(Object value) {
        if (value instanceof String) {
            try {
                Integer i = Integer.valueOf((String) value);
                return checkIntegerOnPositive(i);
            } catch (NumberFormatException e) {
                return false;
            }
        } else if (value instanceof Integer) {
            return checkIntegerOnPositive((Integer) value);
        } else {
            return false;
        }
    }

    private boolean checkIntegerOnPositive(Integer value) {
        if (ObjectUtils.equals("true", onlyPositive)) {
            return value.intValue() >= 0;
        } else {
            return true;
        }
    }

    public void validate(Object value) throws ValidationException {
        if (!isValid(value)) {
            String msg = message != null ? MessageUtils.loadString(message) : "Invalid value '%s'";
            throw new ValidationException(String.format(msg, value));
        }
    }
}
