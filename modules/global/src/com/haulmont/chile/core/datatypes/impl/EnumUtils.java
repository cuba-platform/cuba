/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.chile.core.datatypes.impl;

import javax.annotation.Nullable;

/**
 * @author krivopustov
 * @version $Id$
 */
public class EnumUtils {

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
}
