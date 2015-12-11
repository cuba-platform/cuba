/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.global.filter;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;

/**
 * @author devyatkin
 * @version $Id$
 */
public enum Op {
    EQUAL("=", "==", false),
    IN("in", "in", false),
    NOT_IN("not in", "in", false),
    NOT_EQUAL("<>", "!=", false),
    GREATER(">", ">", false),
    GREATER_OR_EQUAL(">=", ">=", false),
    LESSER("<", "<", false),
    LESSER_OR_EQUAL("<=", "<=", false),
    CONTAINS("like", "contains", false),
    DOES_NOT_CONTAIN("not like", "contains", false),
    NOT_EMPTY("is not null", "!= null", true),
    STARTS_WITH("like", "startsWith", false),
    ENDS_WITH("like", "endsWith", false);

    private String forJpql;
    private String forGroovy;
    private boolean unary;

    Op(String forJpql,String forGroovy, boolean unary) {
        this.forGroovy = forGroovy;
        this.unary = unary;
        this.forJpql = forJpql;
    }

    public String forJpql() {
        return forJpql;
    }

    public String forGroovy() {
        return forGroovy;
    }

    public boolean isUnary() {
        return unary;
    }

    public static Op fromJpqlString(String str) {
        for (Op op : values()) {
            if (op.forJpql.equals(str))
                return op;
        }
        throw new UnsupportedOperationException("Unsupported operation: " + str);
    }

    public static Op fromGroovyString(String str) {
        for (Op op : values()) {
            if (op.forGroovy != null && op.forGroovy.equals(str))
                return op;
        }
        throw new UnsupportedOperationException("Unsupported operation: " + str);
    }

    public String getLocCaption() {
        return AppBeans.get(Messages.class).getMainMessage("Op." + this.name());
    }
}