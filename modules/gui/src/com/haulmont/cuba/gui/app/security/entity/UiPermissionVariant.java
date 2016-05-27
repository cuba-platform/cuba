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

public enum UiPermissionVariant implements EnumClass<Integer> {

    /**
     * Read-only
     */
    READ_ONLY(10, "blue"),

    /**
     * Hide
     */
    HIDE(20, "red"),

    /**
     * Permission not selected
     */
    NOTSET(30, "black"),

    /**
     * Show
     */
    SHOW(40, "green");

    private Integer id;

    private String color;

    UiPermissionVariant(Integer id, String color) {
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

    public static UiPermissionVariant fromId(Integer id) {
        for (UiPermissionVariant variant : UiPermissionVariant.values()) {
            if (ObjectUtils.equals(variant.getId(), id)) {
                return variant;
            }
        }
        return null;
    }
}