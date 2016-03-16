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
 * Role type.
 *
 */
public enum RoleType implements EnumClass<Integer> {

    STANDARD(0) {
        @Override
        public Integer permissionValue(PermissionType type, String target) {
            return null;
        }
    },
    SUPER(10) {
        @Override
        public Integer permissionValue(PermissionType type, String target) {
            return Integer.MAX_VALUE;
        }
    },
    READONLY(20) {
        @Override
        public Integer permissionValue(PermissionType type, String target) {
            switch (type) {
                case ENTITY_ATTR:
                case SCREEN:
                case SPECIFIC:
                    return null;
                case ENTITY_OP:
                    if (target.endsWith(EntityOp.CREATE.getId())
                            || target.endsWith(EntityOp.UPDATE.getId())
                            || target.endsWith(EntityOp.DELETE.getId()))
                        return 0;
                    else
                        return null;
                default:
                    return null;
            }
        }
    },
    DENYING(30) {
        @Override
        public Integer permissionValue(PermissionType type, String target) {
            switch (type) {
                case ENTITY_ATTR:
                    return null;
                case SCREEN:
                case SPECIFIC:
                case ENTITY_OP:
                    return 0;
                default:
                    return null;
            }
        }
    };

    private int id;

    RoleType(int id) {
        this.id = id;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public static RoleType fromId(Integer id) {
        if (id == null)
            return STANDARD; // for backward compatibility, just in case
        for (RoleType type : RoleType.values()) {
            if (ObjectUtils.equals(id, type.getId()))
                return type;
        }
        return null; // unknown id
    }

    public abstract Integer permissionValue(PermissionType type, String target);
}
