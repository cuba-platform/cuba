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

public enum ReportType implements EnumClass<Integer> {
    SIMPLE(10),
    PRINT_FORM(20),
    LIST_PRINT_FORM(30);

    private Integer id;

    public Integer getId() {
        return id;
    }

    ReportType(Integer id) {
        this.id = id;
    }

    public static ReportType fromId(Integer id) {
        for (ReportType type : ReportType.values()) {
            if (ObjectUtils.equals(type.getId(), id)) {
                return type;
            }
        }
        return null;
    }

}