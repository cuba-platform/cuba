/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.gui.components.formatters;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.Formatter;

/**
 * @author tulupov
 * @version $Id$
 */
public class ClassNameFormatter implements Formatter{

    protected Messages messages = AppBeans.get(Messages.class);

    @Override
    public String format(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof String) {
            String str = (String) value;
            final int i = str.lastIndexOf(".");
            if (i < 0) {
                return str;
            } else {
                return messages.getMessage(str.substring(0, i), str.substring(i + 1, str.length()));
            }
        }
        return null;
    }
}
