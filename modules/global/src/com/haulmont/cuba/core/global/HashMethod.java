/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.global;

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