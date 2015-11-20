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
    IN("in", null, false),
    NOT_IN("not in", null, false),
    NOT_EQUAL("<>", "!=", false),
    GREATER(">", ">", false),
    GREATER_OR_EQUAL(">=", ">=", false),
    LESSER("<", "<", false),
    LESSER_OR_EQUAL("<=", "<=", false),
    CONTAINS("like", null, false),
    DOES_NOT_CONTAIN("not like", null, false),
    NOT_EMPTY("is not null", null, true),
    STARTS_WITH("like", null, false),
    ENDS_WITH("like", null, false);

    private String forJpql;
    private String forGroovy;
    private boolean unary;

    Op(String forGroovy, String forJpql, boolean unary) {
        this.forGroovy = forGroovy;
        this.unary = unary;
        this.forJpql = forJpql;
    }

    public String forJpql() {
        return forGroovy;
    }

    public String forGroovy() {
        if (forJpql == null) {
            throw new UnsupportedOperationException("Groovy conditions do not support " + this + " operator");
        }

        return forJpql;
    }

    public boolean isUnary() {
        return unary;
    }

    public static Op fromString(String str) {
        for (Op op : values()) {
            if (op.forGroovy.equals(str))
                return op;
        }
        throw new UnsupportedOperationException("Unsupported operation: " + str);
    }

    public String getLocCaption() {
        return AppBeans.get(Messages.class).getMainMessage("Op." + this.name());
    }
}