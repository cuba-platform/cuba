/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.security.entity;

import com.haulmont.chile.core.datatypes.impl.EnumClass;
import org.apache.commons.lang.ObjectUtils;

/**
 * Type of constraint.
 *
 * @author krivopustov
 * @version $Id$
 */
public enum ConstraintCheckType implements EnumClass<String> {
    DATABASE_AND_MEMORY("db_and_memory", true, true),
    DATABASE("db", true, false),
    MEMORY("memory", false, true);

    private String id;
    private boolean memory;
    private boolean database;

    ConstraintCheckType(String id, boolean database, boolean memory) {
        this.id = id;
        this.database = database;
        this.memory = memory;
    }

    public String getId() {
        return id;
    }

    public static ConstraintCheckType fromId(String id) {
        for (ConstraintCheckType type : ConstraintCheckType.values()) {
            if (ObjectUtils.equals(id, type.getId()))
                return type;
        }
        return null; // unknown id
    }

    public boolean database(){
        return database;
    }

    public boolean memory(){
        return memory;
    }
}
