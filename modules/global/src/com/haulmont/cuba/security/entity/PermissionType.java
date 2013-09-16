/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.security.entity;

import com.haulmont.chile.core.datatypes.impl.EnumClass;
import org.apache.commons.lang.ObjectUtils;

/**
 * Type of permission<br>
 * {@link #id} - corresponding database value
 */
public enum PermissionType implements EnumClass<Integer>
{
    /** Permission to screen */
    SCREEN(10),

    /** Permission to entity operation (see {@link EntityOp}) */
    ENTITY_OP(20),

    /** Permission to entity attribute (see {@link EntityAttrAccess}) */
    ENTITY_ATTR(30),

    /** Application-specific permission */
    SPECIFIC(40),

    /** Permissions for UI components in screens */
    UI(50);

    private int id;

    PermissionType(int id) {
        this.id = id;
    }

    /** Returns corresponding database value */
    @Override
    public Integer getId() {
        return id;
    }

    /** Constructs type from corresponding database value */
    public static PermissionType fromId(Integer id) {
        for (PermissionType type : PermissionType.values()) {
            if (ObjectUtils.equals(type.getId(), id)) {
                return type;
            }
        }
        return null;
    }
}
