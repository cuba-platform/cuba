/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components;

/**
 * @param <P>
 *
 * @author gorodnov
 * @version $Id$
 */
public class AggregationInfo<P> {

    public enum Type {
        SUM,
        AVG,
        COUNT,
        MIN,
        MAX
    }

    private P propertyPath;
    private Type type;
    private Formatter formatter;

    public P getPropertyPath() {
        return propertyPath;
    }

    public void setPropertyPath(P propertyPath) {
        this.propertyPath = propertyPath;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Formatter getFormatter() {
        return formatter;
    }

    public void setFormatter(Formatter formatter) {
        this.formatter = formatter;
    }
}