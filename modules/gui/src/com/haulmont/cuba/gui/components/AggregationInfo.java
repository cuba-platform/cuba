/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 16.10.2009 10:23:53
 *
 * $Id$
 */
package com.haulmont.cuba.gui.components;

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
