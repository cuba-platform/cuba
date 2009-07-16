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

import java.text.NumberFormat;
import java.text.ParseException;

public class DoubleValidator implements Field.Validator{

    protected String message;

    public DoubleValidator(Element element) {
        message = element.attributeValue("message");
    }

    public void validate(Object value) throws ValidationException {
        boolean result;
        if (value instanceof String) {
            try {
                NumberFormat.getInstance().parse((String) value);
                result = true;
            } catch (NumberFormatException e) {
                result = false;
            } catch (ParseException e) {
                result = false;
            }
        } else if (value instanceof Double) {
            result = true;
        } else {
            result = false;
        }
        if (!result) {
            String msg = message != null ? MessageUtils.loadString(message) : "Invalid value '%s'";
            throw new ValidationException(String.format(msg, value));
        }
    }
}
