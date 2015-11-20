/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.global.filter;

public enum LogicalOp {
    AND("and", "&&"),
    OR("or", "||");

    private final String forJpql;
    private final String forGroovy;

    LogicalOp(String forJpql, String forGroovy) {
        this.forJpql = forJpql;
        this.forGroovy = forGroovy;
    }

    public String forJpql() {
        return forJpql;
    }

    public String forGroovy() {
        return forGroovy;
    }

    public static LogicalOp fromString(String str) {
        if (AND.forJpql().equals(str))
            return AND;
        else if (OR.forJpql().equals(str))
            return OR;
        else
            throw new IllegalArgumentException("Invalid LogicalOp: " + str);
    }
}
