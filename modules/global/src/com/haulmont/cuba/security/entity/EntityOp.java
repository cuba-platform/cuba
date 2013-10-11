/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.security.entity;

/**
 * Type of operation on entity.
 *
 * @author krivopustov
 * @version $Id$
 */
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
