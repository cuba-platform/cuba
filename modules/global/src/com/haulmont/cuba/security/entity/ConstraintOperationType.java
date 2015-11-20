/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.security.entity;

import com.haulmont.chile.core.datatypes.impl.EnumClass;
import org.apache.commons.lang.ObjectUtils;

/**
 * Area of constraint application.
 *
 * @author degtyarjov
 * @version $Id$
 */
//todo eude rename to ConstraintOperationType and probably join with EntityOp
public enum ConstraintOperationType implements EnumClass<String> {
    CREATE("create"),
    READ("read"),
    UPDATE("update"),
    DELETE("delete"),
    CUSTOM("custom");

    private String id;

    ConstraintOperationType(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public static ConstraintOperationType fromId(String id) {
        for (ConstraintOperationType area : ConstraintOperationType.values()) {
            if (ObjectUtils.equals(id, area.getId()))
                return area;
        }
        return null; // unknown id
    }
}
