/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.global;

/**
 * This exception is raised when you try to get unexistent view by entity/name
 */
public class ViewNotFoundException extends RuntimeException
{
    private static final long serialVersionUID = -7372799415486288473L;

    public ViewNotFoundException(String message) {
        super(message);
    }
}
