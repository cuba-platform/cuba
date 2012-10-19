/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.shop.core.entity;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

/**
 * @author sukhova
 * @version $Id$
 */
public enum Unit implements EnumClass<String>
{
    PCS("pcs"),
    KG("kg"),
    M("m");

    private String id;

    Unit(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public static Unit fromId(String id) {
        if ("pcs".equals(id))
            return PCS;
        else if ("kg".equals(id))
            return KG;
        else if ("m".equals(id))
            return M;
        else
            return null;
    }
}
