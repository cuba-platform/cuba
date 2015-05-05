/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.chile.core.datatypes.impl;

import javax.annotation.Nullable;

/**
 * A helper class to be used in {@link EnumClass} implementations to convert identifiers to enum values.
 *
 * @author krivopustov
 * @version $Id$
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
