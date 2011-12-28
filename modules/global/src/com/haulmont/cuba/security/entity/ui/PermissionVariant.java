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
