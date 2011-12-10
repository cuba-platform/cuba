/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.security.ui;

import com.haulmont.chile.core.datatypes.impl.EnumClass;
import org.apache.commons.lang.ObjectUtils;

/**
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public enum UiPermissionVariant implements EnumClass<Integer> {

    /**
     * Read-only
     */
    READ_ONLY(10),

    /**
     * Hide
     */
    HIDE(20),

    /**
     * Permission not selected
     */
    NOTSET(30);

    private Integer id;

    @Override
    public Integer getId() {
        return id;
    }

    UiPermissionVariant(Integer id) {
        this.id = id;
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
