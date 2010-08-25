/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 24.06.2010 15:54:28
 *
 * $Id$
 */
package com.haulmont.cuba.core.entity;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

public enum FtsChangeType implements EnumClass<String> {
    INSERT("I"),
    UPDATE("U"),
    DELETE("D");

    private final String id;

    FtsChangeType(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public static FtsChangeType fromId(String id) {
        if ("I".equals(id))
            return INSERT;
        else if ("U".equals(id))
            return UPDATE;
        else if ("D".equals(id))
            return DELETE;
        else
            return null;
    }
}
