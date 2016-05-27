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

public enum SchedulingType implements EnumClass<String> {
    CRON("C"),
    PERIOD("P");

    private final String id;

    SchedulingType(String id) {
        this.id = id;
    }

    @Override
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