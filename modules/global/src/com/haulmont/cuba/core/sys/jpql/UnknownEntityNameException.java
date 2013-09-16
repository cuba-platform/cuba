/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.jpql;

/**
 * User: Alex Chevelev
 * Date: 14.10.2010
 * Time: 0:06:42
 */
public class UnknownEntityNameException extends Throwable {
    private String entityName;

    public UnknownEntityNameException(String entityName) {
        super("Entity with name [" + entityName + "] is unknown");
        this.entityName = entityName;
    }
}
