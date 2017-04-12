/*
 * Copyright (c) 2008-2017 Haulmont.
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

package com.haulmont.cuba.security.entity;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;

/**
 * Type of session action <br>
 * {@link #id} - corresponding value stored in the database
 */
public enum SessionAction implements EnumClass<Integer> {
    LOGIN(1),
    LOGOUT(2),
    EXPIRATION(3),
    TERMINATION(4),
    SUBSTITUTION(5);

    private Integer id;

    SessionAction(Integer value) {
        this.id = value;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static SessionAction fromId(Integer id) {
        for (SessionAction at : SessionAction.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}