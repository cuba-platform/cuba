/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.components.filter;

import com.haulmont.cuba.core.global.MessageProvider;

/**
 * <p>$Id$</p>
 *
 * @author devyatkin
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

    public String toString() {
        return MessageProvider.getMessage(this);
    }
}
