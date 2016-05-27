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

package com.haulmont.cuba.gui.app.security.entity;

import com.haulmont.chile.core.datatypes.impl.EnumClass;
import org.apache.commons.lang.ObjectUtils;

public enum PermissionVariant implements EnumClass<Integer> {

    /**
     * Allowed
     */
    ALLOWED(10, "green"),

    /**
     * Disallowed
     */
    DISALLOWED(20, "red"),

    /**
     * Permission not selected
     */
    NOTSET(30, "black");

    private Integer id;

    private String color;

    PermissionVariant(Integer id, String color) {
        this.id = id;
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public static PermissionVariant fromId(Integer id) {
        for (PermissionVariant variant : PermissionVariant.values()) {
            if (ObjectUtils.equals(variant.getId(), id)) {
                return variant;
            }
        }
        return null;
    }
}