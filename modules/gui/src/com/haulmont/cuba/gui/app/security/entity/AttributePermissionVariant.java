/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.app.security.entity;

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
