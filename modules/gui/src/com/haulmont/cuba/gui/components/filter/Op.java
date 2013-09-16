/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.filter;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.MessageProvider;

import java.util.Date;
import java.util.EnumSet;
import java.util.UUID;

/**
 * <p>$Id$</p>
 *
 * @author devyatkin
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
    EMPTY("is null", true),
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

    public static EnumSet<Op> availableOps(Class javaClass) {
        if (String.class.equals(javaClass))
            return EnumSet.of(EQUAL, IN, NOT_IN, NOT_EQUAL, CONTAINS, DOES_NOT_CONTAIN, EMPTY, NOT_EMPTY, STARTS_WITH, ENDS_WITH);

        else if (Date.class.isAssignableFrom(javaClass)
                || Number.class.isAssignableFrom(javaClass))
            return EnumSet.of(EQUAL, IN, NOT_IN, NOT_EQUAL, GREATER, GREATER_OR_EQUAL, LESSER, LESSER_OR_EQUAL, EMPTY, NOT_EMPTY);

        else if (Boolean.class.equals(javaClass))
            return EnumSet.of(EQUAL, NOT_EQUAL, EMPTY, NOT_EMPTY);

        else if (UUID.class.equals(javaClass)
                || Enum.class.isAssignableFrom(javaClass)
                || Entity.class.isAssignableFrom(javaClass))
            return EnumSet.of(EQUAL, IN, NOT_IN, NOT_EQUAL, EMPTY, NOT_EMPTY);

        else
            throw new UnsupportedOperationException("Unsupported java class: " + javaClass);
    }

    public String toString() {
        return MessageProvider.getMessage(this);
    }
}
