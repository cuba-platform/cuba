/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.security.entity;

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
