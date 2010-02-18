/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Maksim Tulupov
 * Created: 15.02.2010 11:25:59
 *
 * $Id$
 */
package com.haulmont.cuba.security.entity;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

public enum AttributeType implements EnumClass<String>{
    STRING("STRING"),
    INTEGER("INTEGER"),
    DOUBLE("DOUBLE"),
    BOOLEAN("BOOLEAN"),
    DATE("DATE"),
    DATE_TIME("DATE_TIME"),
    ENTITY("ENTITY");

    private String id;

    AttributeType(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    public static AttributeType fromId(String id) {
        for (AttributeType at : AttributeType.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}
