/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 29.09.2009 11:42:31
 *
 * $Id$
 */
package com.haulmont.cuba.gui.filter;

public enum LogicalOp {
    AND("and"),
    OR("or");

    private final String text;

    LogicalOp(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public static LogicalOp fromString(String str) {
        if (AND.getText().equals(str))
            return AND;
        else if (OR.getText().equals(str))
            return OR;
        else
            throw new IllegalArgumentException("Invalid LogicalOp: " + str);
    }
}
