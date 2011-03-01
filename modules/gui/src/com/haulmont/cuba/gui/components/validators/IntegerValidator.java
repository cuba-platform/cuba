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

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.core.global.MessageUtils;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.ValidationException;
import org.apache.commons.lang.ObjectUtils;
import org.dom4j.Element;

import java.text.ParseException;

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

    public IntegerValidator() {
        this.message = MessageProvider.getMessage(AppConfig.getInstance().getMessagesPack(), "validation.invalidNumber");
    }

    private boolean checkIntegerOnPositive(Integer value) {
        return !ObjectUtils.equals("true", onlyPositive) || value >= 0;
    }

    public void validate(Object value) throws ValidationException {
        boolean result;
        if (value instanceof String) {
            try {
                Number num = ValidationHelper.parseNumber((String) value, MessageUtils.getIntegerFormat());
                result = checkIntegerOnPositive(num.intValue());
            } catch (ParseException e) {
                result = false;
            }
        } else {
            result = value instanceof Integer && checkIntegerOnPositive((Integer) value);
        }
        if (!result) {
            String msg = message != null ? MessageUtils.loadString(messagesPack, message) : "Invalid value '%s'";
            throw new ValidationException(String.format(msg, value));
        }
    }
}
