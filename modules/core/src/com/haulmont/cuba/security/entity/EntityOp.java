/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 25.09.2009 17:06:19
 *
 * $Id$
 */
package com.haulmont.cuba.security.entity;

public enum EntityOp {
    READ("read"),
    CREATE("create"),
    UPDATE("update"),
    DELETE("delete");

    private String id;

    EntityOp(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
