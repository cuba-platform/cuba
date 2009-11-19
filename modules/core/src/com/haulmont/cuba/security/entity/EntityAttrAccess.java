/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 25.09.2009 17:18:24
 *
 * $Id$
 */
package com.haulmont.cuba.security.entity;

public enum EntityAttrAccess {
    DENY(0),
    VIEW(1),
    MODIFY(2);

    private int id;

    EntityAttrAccess(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static EntityAttrAccess fromId(int id) {
        switch (id) {
            case 0: return DENY;
            case 1: return VIEW;
            case 2: return MODIFY;
            default: return null;
        }
    }
}
