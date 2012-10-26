/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.entity;

import com.haulmont.chile.core.datatypes.impl.EnumClass;
import org.apache.commons.lang.ObjectUtils;

/**
 * @author artamonov
 * @version $Id$
 */
public enum HashMethod implements EnumClass<String> {
    MD5("md5"),
    SHA1("sha1");

    private String id;

    HashMethod(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    public static HashMethod fromId(String id) {
        for (HashMethod method : HashMethod.values()) {
            if (ObjectUtils.equals(method.getId(), id)) {
                return method;
            }
        }
        return null;
    }
}