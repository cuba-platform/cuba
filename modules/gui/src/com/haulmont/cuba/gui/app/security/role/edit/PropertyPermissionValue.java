/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.app.security.role.edit;

public enum PropertyPermissionValue {
    MODIFY(2),
    VIEW(1),
    DENY(0);

    private int value;

    PropertyPermissionValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
