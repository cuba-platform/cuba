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

/**
 *
 */
public enum AttributePermissionVariant implements EnumClass<Integer> {

    /**
     * Full access
     */
    MODIFY(10, "green"),

    /**
     * Read-only
     */
    READ_ONLY(20, "blue"),

    /**
     * Hide
     */
    HIDE(30, "red"),

    /**
     * Permission not selected
     */
    NOTSET(40, "black");

    private Integer id;

    private String color;

    AttributePermissionVariant(Integer id, String color) {
        this.id = id;
        this.color = color;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public String getColor() {
        return color;
    }

    public static AttributePermissionVariant fromId(Integer id) {
        for (AttributePermissionVariant variant : AttributePermissionVariant.values()) {
            if (ObjectUtils.equals(variant.getId(), id)) {
                return variant;
            }
        }
        return null;
    }
}
