/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 25.09.2009 10:12:51
 *
 * $Id$
 */
package com.haulmont.cuba.web.app.ui.security.role.edit;

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
