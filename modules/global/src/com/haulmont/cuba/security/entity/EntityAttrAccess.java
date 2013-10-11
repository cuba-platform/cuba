/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.security.entity;

/**
 * Type of access to an entity attribute.
 *
 * @author krivopustov
 * @version $Id$
 */
public enum EntityAttrAccess {
    DENY(0),
    VIEW(1),
    MODIFY(2);

    private int id;

    EntityAttrAccess(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static EntityAttrAccess fromId(int id) {
        switch (id) {
            case 0: return DENY;
            case 1: return VIEW;
            case 2: return MODIFY;
            default: return null;
        }
    }
}
