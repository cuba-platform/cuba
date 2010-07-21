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

public enum DataSetType implements EnumClass<Integer> {
    SQL(10),
    JPQL(20),
    GROOVY(30),
    SINGLE(40),
    MULTI(50);


    private Integer id;

    public Integer getId() {
        return id;
    }

    DataSetType(Integer id) {
        this.id = id;
    }

    public static DataSetType fromId(Integer id) {
        for (DataSetType type : DataSetType.values()) {
            if (ObjectUtils.equals(type.getId(), id)) {
                return type;
            }
        }
        return null;
    }

}
