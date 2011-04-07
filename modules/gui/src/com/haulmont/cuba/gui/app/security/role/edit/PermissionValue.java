/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.gui.app.security.role.edit;

enum PermissionValue {
    ALLOW(1),
    DENY(0);

    private int value;

    PermissionValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
