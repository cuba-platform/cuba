/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.core.global;

import com.haulmont.cuba.core.sys.ClassesInfo;
import com.haulmont.cuba.security.entity.PermissionType;

/**
 * Exception that is raised on attempt to violate a security constraint.
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class AccessDeniedException extends RuntimeException
{
    private static final long serialVersionUID = -3097861878301424338L;

    static {
        ClassesInfo.addClientSupported(AccessDeniedException.class);
    }

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
