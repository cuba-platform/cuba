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

package com.haulmont.chile.core.datatypes.impl;

import javax.annotation.Nullable;

/**
 * A helper class to be used in {@link EnumClass} implementations to convert identifiers to enum values.
 *
 */
public class EnumUtils {

    /**
     * Returns an enum value for the given id.
     * @param e     enum class
     * @param id    id
     * @return      enum value or null if the passed id is null
     * @throws IllegalArgumentException if there are no enum values with the given id
     */
    @Nullable
    public static <T extends Enum<T> & EnumClass<V>, V> T fromId(Class<T> e, V id) {
        if (id == null)
            return null;
        for (T enumConstant : e.getEnumConstants()) {
            if (enumConstant.getId().equals(id)) {
                return enumConstant;
            }
        }
        throw new IllegalArgumentException("Can't parse " + e.getSimpleName() + " from id " + id);
    }

    /**
     * Returns an enum value for the given id.
     * @param e             enum class
     * @param id            id
     * @param defaultValue  the value to return if null is passed as id
     * @return              enum value
     * @throws IllegalArgumentException if there are no enum values with the given id
     */
    public static <T extends Enum<T> & EnumClass<V>, V> T fromId(Class<T> e, V id, T defaultValue) {
        if (id == null)
            return defaultValue;
        for (T enumConstant : e.getEnumConstants()) {
            if (enumConstant.getId().equals(id)) {
                return enumConstant;
            }
        }
        throw new IllegalArgumentException("Can't parse " + e.getSimpleName() + " from id " + id);
    }

    /**
     * Returns an enum value for the given id, or the default value if null is passed or there are no enum values with
     * the given id.
     * @param e             enum class
     * @param id            id
     * @param defaultValue  the value to return if null is passed as id or if there are no enum values with the given id
     * @return              enum value
     */
    public static <T extends Enum<T> & EnumClass<V>, V> T fromIdSafe(Class<T> e, V id, T defaultValue) {
        if (id == null)
            return defaultValue;
        for (T enumConstant : e.getEnumConstants()) {
            if (enumConstant.getId().equals(id)) {
                return enumConstant;
            }
        }
        return defaultValue;
    }
}
