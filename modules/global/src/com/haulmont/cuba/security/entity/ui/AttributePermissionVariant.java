/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.security.entity.ui;

import com.haulmont.chile.core.datatypes.impl.EnumClass;
import org.apache.commons.lang.ObjectUtils;

/**
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public enum AttributePermissionVariant implements EnumClass<Integer> {

    /**
     * Full access
     */
    MODIFY(10),

    /**
     * Read-only
     */
    READ_ONLY(20),

    /**
     * Hide
     */
    HIDE(30),

    /**
     * Permission not selected
     */
    NOTSET(40);

    private Integer id;

    @Override
    public Integer getId() {
        return id;
    }

    AttributePermissionVariant(Integer id) {
        this.id = id;
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
