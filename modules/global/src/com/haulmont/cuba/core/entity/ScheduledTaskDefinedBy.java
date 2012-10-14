/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
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
