/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.entity;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

public enum ScheduledTaskDefinedBy implements EnumClass<String> {
    BEAN("B"),
    CLASS("C"),
    SCRIPT("S");

    private final String id;

    ScheduledTaskDefinedBy(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public static String getId(ScheduledTaskDefinedBy scheduledTask) {
        return scheduledTask != null ? scheduledTask.getId() : null;
    }

    public static ScheduledTaskDefinedBy fromId(String id) {
        for (ScheduledTaskDefinedBy currentTask : values()) {
            if (currentTask.getId().equals(id)) {
                return currentTask;
            }
        }
        return null;
    }
}
