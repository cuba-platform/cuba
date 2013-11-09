/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components.validators;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.ValidationException;
import org.dom4j.Element;

import java.util.regex.Pattern;

/**
 * @author abramov
 * @version $Id$
 */
public class PatternValidator implements Field.Validator {

    protected Pattern pattern;
    protected String message;
    protected String messagesPack;
    protected Messages messages = AppBeans.get(Messages.class);

    public PatternValidator(Element element, String messagesPack) {
        this(element.attributeValue("pattern"));
        message = element.attributeValue("message");
        this.messagesPack = messagesPack;
    }

    public PatternValidator(String pattern) {
        this.pattern = Pattern.compile(pattern);
    }

    @Override
    public void validate(Object value) throws ValidationException {
        if (value == null || !pattern.matcher(((String) value)).matches()) {
            String msg = message != null ? messages.getTools().loadString(messagesPack, message) : "Invalid value '%s'";
            throw new ValidationException(String.format(msg, value != null ? value : ""));
        }
    }
}