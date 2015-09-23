/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.entity;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

/**
 * @author krivopustov
 * @version $Id$
 */
public enum FtsChangeType implements EnumClass<String> {
    INSERT("I"),
    UPDATE("U"),
    DELETE("D");

    private final String id;

    FtsChangeType(String id) {
        this.id = id;
    }

    @Override
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