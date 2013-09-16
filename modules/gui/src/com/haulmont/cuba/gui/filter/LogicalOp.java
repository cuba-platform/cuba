/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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
