/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.validators;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.ValidationException;
import org.dom4j.Element;

/**
 * @author gorelov
 * @version $Id$
 */
public class StringValidator implements Field.Validator {

    protected String message;
    protected String messagesPack;
    protected Messages messages = AppBeans.get(Messages.NAME);

    public StringValidator(Element element, String messagesPack) {
        this.message = element.attributeValue("message");
        this.messagesPack = messagesPack;
    }

    public StringValidator(String message) {
        this.message = message;
    }

    @Override
    public void validate(Object value) throws ValidationException {
        if (!(value instanceof String)) {
            String msg = message != null ? messages.getTools().loadString(messagesPack, message) : "Invalid value '%s'";
            throw new ValidationException(String.format(msg, value));
        }
    }
}
