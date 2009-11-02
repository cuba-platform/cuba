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

import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.cuba.core.global.MessageUtils;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.ValidationException;
import org.dom4j.Element;

import java.text.ParseException;

public class DoubleValidator implements Field.Validator {

    protected String message;
    protected String messagesPack;

    public DoubleValidator(Element element, String messagesPack) {
        message = element.attributeValue("message");
        this.messagesPack = messagesPack;
    }

    public DoubleValidator(String message) {
        this.message = message;
    }

    public void validate(Object value) throws ValidationException {
        boolean result;
        if (value instanceof String) {
            try {
                Datatypes.getInstance().get(Double.class).parse((String) value);
                result = true;
            } catch (ParseException e) {
                result = false;
            }
        } else if (value instanceof Double) {
            result = true;
        } else {
            result = false;
        }
        if (!result) {
            String msg = message != null ? MessageUtils.loadString(messagesPack, message) : "Invalid value '%s'";
            throw new ValidationException(String.format(msg, value));
        }
    }
}
