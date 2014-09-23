/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components.validators;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.datatypes.impl.DateDatatype;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.ValidationException;
import org.dom4j.Element;

import java.text.ParseException;
import java.util.Date;

/**
 * @author krivopustov
 * @version $Id$
 */
public class DateValidator implements Field.Validator {

    protected String message;
    protected String messagesPack;
    protected Messages messages = AppBeans.get(Messages.NAME);

    public DateValidator(Element element, String messagesPack) {
        this.message = element.attributeValue("message");
        this.messagesPack = messagesPack;
    }

    public DateValidator(String message) {
        this.message = message;
    }

    public DateValidator() {
        this.message = messages.getMainMessage("validation.invalidDate");
    }

    @Override
    public void validate(Object value) throws ValidationException {
        if (value == null)
            return;

        boolean result;
        if (value instanceof String) {
            try {
                Datatype datatype = Datatypes.get(DateDatatype.NAME);
                UserSessionSource sessionSource = AppBeans.get(UserSessionSource.NAME);
                datatype.parse((String) value, sessionSource.getLocale());
                result = true;
            } catch (ParseException e) {
                result = false;
            }
        } else {
            result = value instanceof Date;
        }
        if (!result) {
            String msg = message != null ? messages.getTools().loadString(messagesPack, message) : "Invalid value '%s'";
            throw new ValidationException(String.format(msg, value));
        }
    }
}