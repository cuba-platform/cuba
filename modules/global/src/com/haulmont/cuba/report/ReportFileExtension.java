/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Artamonov Yuryi
 * Created: 30.03.11 9:38
 *
 * $Id$
 */
package com.haulmont.cuba.report;

import com.haulmont.chile.core.datatypes.impl.EnumClass;
import org.apache.commons.lang.ObjectUtils;

public enum ReportFileExtension implements EnumClass<String> {
    XLT("xlt"),
    XLS("xls"),
    DOC("doc"),
    ODT("odt"),
    HTML("html"),
    HTM("htm");

    private String id;

    ReportFileExtension(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public static ReportFileExtension fromId(String id) {
        for (ReportFileExtension type : ReportFileExtension.values()) {
            if (ObjectUtils.equals(type.getId(), id)) {
                return type;
            }
        }
        return null;
    }
}