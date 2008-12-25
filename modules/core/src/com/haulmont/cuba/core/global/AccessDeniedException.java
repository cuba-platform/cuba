/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 25.12.2008 13:24:09
 *
 * $Id$
 */
package com.haulmont.cuba.core.global;

import com.haulmont.cuba.security.entity.PermissionType;

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
