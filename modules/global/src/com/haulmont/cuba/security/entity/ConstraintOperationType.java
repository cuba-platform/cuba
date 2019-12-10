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

import java.util.Collections;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

/**
 * Area of constraint application.
 */
public enum ConstraintOperationType implements EnumClass<String> {

    //todo degtyarjov probably need to join with EntityOp

    CREATE("create"),
    READ("read"),
    UPDATE("update"),
    DELETE("delete"),
    ALL("all"),
    CUSTOM("custom");

    private String id;

    ConstraintOperationType(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    public Set<EntityOp> toEntityOps() {
        switch (this) {
            case CREATE:
                return EnumSet.of(EntityOp.CREATE);
            case READ:
                return EnumSet.of(EntityOp.READ);
            case UPDATE:
                return EnumSet.of(EntityOp.UPDATE);
            case DELETE:
                return EnumSet.of(EntityOp.DELETE);
            case ALL:
                return EnumSet.allOf(EntityOp.class);
            default:
                return Collections.emptySet();
        }
    }

    public static ConstraintOperationType fromEntityOp(EntityOp entityOp) {
        switch (entityOp) {
            case CREATE:
                return ConstraintOperationType.CREATE;
            case READ:
                return ConstraintOperationType.READ;
            case UPDATE:
                return ConstraintOperationType.UPDATE;
            case DELETE:
                return ConstraintOperationType.DELETE;
        }
        return null; // unknown id
    }

    public static ConstraintOperationType fromId(String id) {
        for (ConstraintOperationType area : ConstraintOperationType.values()) {
            if (Objects.equals(id, area.getId()))
                return area;
        }
        return null; // unknown id
    }
}