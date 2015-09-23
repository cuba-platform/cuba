/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.jpql;

/**
 * @author chevelev
 * @version $Id$
 */
public class UnknownEntityNameException extends Throwable {
    private String entityName;

    public UnknownEntityNameException(String entityName) {
        super("Entity with name [" + entityName + "] is unknown");
        this.entityName = entityName;
    }
}