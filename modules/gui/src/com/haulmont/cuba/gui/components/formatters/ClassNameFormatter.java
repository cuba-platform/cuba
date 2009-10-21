/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Maksim Tulupov
 * Created: 21.10.2009 14:18:03
 *
 * $Id$
 */
package com.haulmont.cuba.gui.components.formatters;

import com.haulmont.cuba.gui.components.Formatter;
import com.haulmont.cuba.core.global.MessageProvider;

public class ClassNameFormatter implements Formatter{
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
                return MessageProvider.getMessage(str.substring(0, i), str.substring(i + 1, str.length()));
            }
        }
        return null;
    }
}
