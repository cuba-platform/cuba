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

import com.haulmont.chile.core.datatypes.impl.EnumClass;
import org.apache.commons.lang.ObjectUtils;

/**
 * Type of permission<br>
 * id - corresponding database value
 */
public enum PermissionType implements EnumClass<Integer>
{
    SCREEN(10),
    ENTITY_OP(20),
    ENTITY_ATTR(30),
    SPECIFIC(40);

    private int id;

    PermissionType(int id) {
        this.id = id;
    }

    /** Returns corresponding database value */
    public Integer getValue() {
        return id;
    }

    /** Constructs type from corresponding database value */
    public static PermissionType valueOf(Integer id) {
        for (PermissionType type : PermissionType.values()) {
            if (ObjectUtils.equals(type.getValue(), id)) {
                return type;
            }
        }
        return null;
    }
}
