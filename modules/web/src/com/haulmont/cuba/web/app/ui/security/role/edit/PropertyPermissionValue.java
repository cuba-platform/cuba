/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 25.09.2009 11:07:34
 *
 * $Id$
 */
package com.haulmont.cuba.web.app.ui.security.role.edit;

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
