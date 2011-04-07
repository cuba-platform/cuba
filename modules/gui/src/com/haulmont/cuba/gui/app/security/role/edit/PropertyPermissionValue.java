/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
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
