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

    /**
     * SQL query
     */
    SQL(10),

    /**
     * JPQL query
     */
    JPQL(20),

    /**
     * Groovy script
     */
    GROOVY(30),

    /**
     * Entity
     */
    SINGLE(40),

    /**
     * Entities list
     */
    MULTI(50);

    private Integer id;

    @Override
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