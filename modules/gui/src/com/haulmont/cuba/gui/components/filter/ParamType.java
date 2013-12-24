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
public enum ParamType {
    STRING,
    DATE,
    DATETIME,
    DOUBLE,
    BIGDECIMAL,
    INTEGER,
    LONG,
    BOOLEAN,
    UUID,
    ENUM,
    ENTITY,
    UNARY;

    @Override
    public String toString() {
        return AppBeans.get(Messages.class).getMessage(this);
    }
}