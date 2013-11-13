/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.entity;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

public enum SchedulingType implements EnumClass<String> {
    CRON("C"),
    PERIOD("P");

    private final String id;

    SchedulingType(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public static String getId(SchedulingType schedulingType) {
        return schedulingType != null ? schedulingType.getId() : null;
    }

    public static SchedulingType fromId(String id) {
        for (SchedulingType schedulingType : values()) {
            if (schedulingType.getId().equals(id)) {
                return schedulingType;
            }
        }
        return null;
    }
}
