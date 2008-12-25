/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 24.12.2008 15:14:08
 *
 * $Id$
 */
package com.haulmont.cuba.security.entity;

public enum PermissionType
{
    ACTION(10),
    ENTITY_OP(20),
    ENTITY_ATTR(30),
    SPECIFIC(40);

    private int id;

    PermissionType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static PermissionType fromId(int id) {
        for (PermissionType type : PermissionType.values()) {
            if (type.getId() == id) {
                return type;
            }
        }
        return null;
    }
}
