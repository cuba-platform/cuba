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
 */

package com.haulmont.cuba.core.entity;

import com.haulmont.chile.core.datatypes.impl.EnumClass;
import org.apache.commons.lang.ObjectUtils;

public enum LockDescriptorNameType implements EnumClass<String> {
    ENTITY("E"),
    CUSTOM("C");

    private final String id;

    LockDescriptorNameType(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    public static LockDescriptorNameType fromId(String id) {
        for (LockDescriptorNameType type : LockDescriptorNameType.values()) {
            if (ObjectUtils.equals(id, type.getId()))
                return type;
        }
        return null; // unknown id
    }
}
