/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components.validators;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.datatypes.impl.IntegerDatatype;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.ValidationException;
import org.apache.commons.lang.ObjectUtils;
import org.dom4j.Element;

import java.text.ParseException;

/**
 * @author tulupov
 * @version $Id$
 */
public class IntegerValidator implements Field.Validator {

    protected String message;
    protected String messagesPack;
    protected String onlyPositive;
    protected Messages messages = AppBeans.get(Messages.NAME);

    public IntegerValidator(Element element, String messagesPack) {
        message = element.attributeValue("message");
        onlyPositive = element.attributeValue("onlyPositive");
        this.messagesPack = messagesPack;
    }

    public IntegerValidator(String message) {
        this.message = message;
    }

    public IntegerValidator() {
        this.message = messages.getMainMessage("validation.invalidNumber");
    }

    private boolean checkIntegerOnPositive(Integer value) {
        return !ObjectUtils.equals("true", onlyPositive) || value >= 0;
    }

    @Override
    public void validate(Object value) throws ValidationException {
        boolean result;
        if (value instanceof String) {
            try {
                Datatype<Integer> datatype = Datatypes.get(IntegerDatatype.NAME);
                UserSessionSource sessionSource = AppBeans.get(UserSessionSource.NAME);
                Integer num = datatype.parse((String) value, sessionSource.getLocale());
                result = checkIntegerOnPositive(num);
            } catch (ParseException e) {
                result = false;
            }
        } else {
            result = value instanceof Integer && checkIntegerOnPositive((Integer) value);
        }
        if (!result) {
            String msg = message != null ? messages.getTools().loadString(messagesPack, message) : "Invalid value '%s'";
            throw new ValidationException(String.format(msg, value));
        }
    }
}