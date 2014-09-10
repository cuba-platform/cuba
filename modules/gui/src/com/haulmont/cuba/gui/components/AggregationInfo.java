/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.chile.core.model.MetaPropertyPath;

/**
 * @author gorodnov
 * @version $Id$
 */
public class AggregationInfo {

    public enum Type {
        SUM,
        AVG,
        COUNT,
        MIN,
        MAX
    }

    private MetaPropertyPath propertyPath;
    private Type type;
    private Formatter formatter;

    public MetaPropertyPath getPropertyPath() {
        return propertyPath;
    }

    public void setPropertyPath(MetaPropertyPath propertyPath) {
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