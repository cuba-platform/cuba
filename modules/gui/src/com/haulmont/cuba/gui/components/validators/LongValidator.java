/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components.validators;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.datatypes.impl.LongDatatype;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.ValidationException;
import org.apache.commons.lang.ObjectUtils;
import org.dom4j.Element;

import java.text.ParseException;

/**
 * @author budarov
 * @version $Id$
 */
public class LongValidator implements Field.Validator {

    protected String message;
    protected String messagesPack;
    protected String onlyPositive;
    protected Messages messages = AppBeans.get(Messages.NAME);

    public LongValidator(Element element, String messagesPack) {
        message = element.attributeValue("message");
        onlyPositive = element.attributeValue("onlyPositive");
        this.messagesPack = messagesPack;
    }

    public LongValidator(String message) {
        this.message = message;
    }

    private boolean checkPositive(Long value) {
        return !ObjectUtils.equals("true", onlyPositive) || value != null && value >= 0;
    }

    @Override
    public void validate(Object value) throws ValidationException {
        boolean result;
        if (value instanceof String) {
            try {
                Datatype<Long> datatype = Datatypes.get(LongDatatype.NAME);
                UserSessionSource sessionSource = AppBeans.get(UserSessionSource.NAME);
                Long num = datatype.parse((String) value, sessionSource.getLocale());
                result = checkPositive(num);
            }
            catch (ParseException e) {
                result = false;
            }
        }
        else {
            result = value instanceof Long && checkPositive((Long) value);
        }
        if (!result) {
            String msg = message != null ? messages.getTools().loadString(messagesPack, message) : "Invalid value '%s'";
            throw new ValidationException(String.format(msg, value));
        }
    }
}