/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.haulmont.cuba.security.entity;

import com.haulmont.chile.core.datatypes.impl.EnumClass;
import org.apache.commons.lang.ObjectUtils;

/**
 * Type of permission.
 * <p/>
 * {@link #id} - corresponding value stored in the database
 *
 */
public enum PermissionType implements EnumClass<Integer> {

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
