/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.app.security.role.edit;

public enum UiPermissionValue {

    SHOW(2),
    READ_ONLY(1),
    HIDE(0);

    private int value;

    UiPermissionValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
