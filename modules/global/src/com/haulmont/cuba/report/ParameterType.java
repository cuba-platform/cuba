/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Eugeniy Degtyarjov
 * Created: 12.05.2010 10:01:06
 *
 * $Id$
 */
package com.haulmont.cuba.report;

import com.haulmont.chile.core.datatypes.impl.EnumClass;
import org.apache.commons.lang.ObjectUtils;

public enum ParameterType implements EnumClass<Integer> {
    DATE(10),
    TEXT(20),
    ENTITY(30),
    BOOLEAN(40),
    NUMERIC(50),
    ENTITY_LIST(60),
    ENUMERATION(70);

    private Integer id;

    public Integer getId() {
        return id;
    }

    ParameterType(Integer id) {
        this.id = id;
    }

    public static ParameterType fromId(Integer id) {
        for (ParameterType type : ParameterType.values()) {
            if (ObjectUtils.equals(type.getId(), id)) {
                return type;
            }
        }
        return null;
    }

}