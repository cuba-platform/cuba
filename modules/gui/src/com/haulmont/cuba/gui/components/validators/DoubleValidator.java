/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components.validators;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.datatypes.impl.DoubleDatatype;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.ValidationException;
import org.apache.commons.lang.ObjectUtils;
import org.dom4j.Element;

import java.math.BigDecimal;
import java.text.ParseException;

/**
 * @author tulupov
 * @version $Id$
 */
public class DoubleValidator implements Field.Validator {

    protected String message;
    protected String messagesPack;
    protected String onlyPositive;
    protected Messages messages = AppBeans.get(Messages.class);

    public DoubleValidator(Element element, String messagesPack) {
        message = element.attributeValue("message");
        onlyPositive = element.attributeValue("onlyPositive");
        this.messagesPack = messagesPack;
    }

    public DoubleValidator(String message) {
        this.message = message;
    }

    public DoubleValidator() {
        this.message = messages.getMainMessage("validation.invalidNumber");
    }

    private boolean checkDoubleOnPositive(Double value) {
        return !ObjectUtils.equals("true", onlyPositive) || value >= 0;
    }

    private boolean checkBigDecimalOnPositive(BigDecimal value) {
        return !ObjectUtils.equals("true", onlyPositive) || value.compareTo(BigDecimal.ZERO) >= 0;
    }

    @Override
    public void validate(Object value) throws ValidationException {
        boolean result;
        if (value instanceof String) {
            try {
                Datatype<Double> datatype = Datatypes.get(DoubleDatatype.NAME);
                Double num = datatype.parse((String) value, AppBeans.get(UserSessionSource.class).getLocale());
                result = checkDoubleOnPositive(num);
            } catch (ParseException e) {
                result = false;
            }
        } else {
            result = (value instanceof Double && checkDoubleOnPositive((Double) value)) || (value instanceof BigDecimal && checkBigDecimalOnPositive((BigDecimal) value));
        }

        if (!result) {
            String msg = message != null ? messages.getTools().loadString(messagesPack, message) : "Invalid value '%s'";
            throw new ValidationException(String.format(msg, value));
        }
    }
}