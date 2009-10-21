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
    protected String messagesPack;
    protected String onlyPositive;

    public IntegerValidator(Element element, String messagesPack) {
        message = element.attributeValue("message");
        onlyPositive = element.attributeValue("onlyPositive");
        this.messagesPack = messagesPack;
    }

    public IntegerValidator(String message) {
        this.message = message;
    }

    private boolean checkIntegerOnPositive(Integer value) {
        if (ObjectUtils.equals("true", onlyPositive)) {
            return value.intValue() >= 0;
        } else {
            return true;
        }
    }

    public void validate(Object value) throws ValidationException {
        boolean result;
        if (value instanceof String) {
            try {
                Integer i = Integer.valueOf((String) value);
                result = checkIntegerOnPositive(i);
            } catch (NumberFormatException e) {
                result = false;
            }
        } else if (value instanceof Integer) {
            result = checkIntegerOnPositive((Integer) value);
        } else {
            result = false;
        }
        if (!result) {
            String msg = message != null ? MessageUtils.loadString(messagesPack, message) : "Invalid value '%s'";
            throw new ValidationException(String.format(msg, value));
        }
    }
}
