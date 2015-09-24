/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.filter;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;

/**
 * @author devyatkin
 * @version $Id$
 */
public enum Op {
    EQUAL("=", false),
    IN("in", false),
    NOT_IN("not in", false),
    NOT_EQUAL("<>", false),
    GREATER(">", false),
    GREATER_OR_EQUAL(">=", false),
    LESSER("<", false),
    LESSER_OR_EQUAL("<=", false),
    CONTAINS("like", false),
    DOES_NOT_CONTAIN("not like", false),
    NOT_EMPTY("is not null", true),
    STARTS_WITH("like", false),
    ENDS_WITH("like", false);

    private String text;
    private boolean unary;

    Op(String text, boolean unary) {
        this.text = text;
        this.unary = unary;
    }

    public String getText() {
        return text;
    }

    public boolean isUnary() {
        return unary;
    }

    public static Op fromString(String str) {
        for (Op op : values()) {
            if (op.text.equals(str))
                return op;
        }
        throw new UnsupportedOperationException("Unsupported operation: " + str);
    }

    public String getLocCaption() {
        return AppBeans.get(Messages.class).getMainMessage("Op." + this.name());
    }
}