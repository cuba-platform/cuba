/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.global;

import com.haulmont.cuba.security.entity.PermissionType;

/**
 * Exception that is raised on attempt to violate a security constraint.
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
@SupportedByClient
public class AccessDeniedException extends RuntimeException
{
    private static final long serialVersionUID = -3097861878301424338L;

    private PermissionType type;

    private String target;

    public AccessDeniedException(PermissionType type, String target) {
        super(type.toString() + " " + target);
        this.type = type;
        this.target = target;
    }

    public PermissionType getType() {
        return type;
    }

    public String getTarget() {
        return target;
    }
}
