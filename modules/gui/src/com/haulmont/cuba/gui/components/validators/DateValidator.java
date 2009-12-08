/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 21.10.2009 11:50:39
 *
 * $Id$
 */
package com.haulmont.cuba.gui.components.validators;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.datatypes.impl.DateDatatype;
import com.haulmont.cuba.core.global.MessageUtils;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.ValidationException;
import org.dom4j.Element;

import java.text.ParseException;
import java.util.Date;

public class DateValidator implements Field.Validator {

    protected String message;
    protected String messagesPack;

    public DateValidator(Element element, String messagesPack) {
        this.message = element.attributeValue("message");
        this.messagesPack = messagesPack;
    }

    public DateValidator(String message) {
        this.message = message;
    }

    public void validate(Object value) throws ValidationException {
        if (value == null)
            return;

        boolean result;
        if (value instanceof String) {
            try {
                Datatype<DateDatatype> datatype = Datatypes.getInstance().get(DateDatatype.NAME);
                datatype.parse((String) value);
                result = true;
            } catch (ParseException e) {
                result = false;
            }
        } else if (value instanceof Date) {
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
