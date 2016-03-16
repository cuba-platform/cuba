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
package com.haulmont.cuba.core.entity;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

/**
 */
public enum ScheduledTaskDefinedBy implements EnumClass<String> {
    BEAN("B"),
    CLASS("C"),
    SCRIPT("S");

    private final String id;

    ScheduledTaskDefinedBy(String id) {
        this.id = id;
    }

    @Override
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