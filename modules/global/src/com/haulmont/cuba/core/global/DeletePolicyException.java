/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 26.01.2009 14:26:15
 *
 * $Id$
 */
package com.haulmont.cuba.core.global;

/**
 * This exception is raised on attempt to soft delete an object,
 * which has linked objects marked with {@link OnDelete} annotation 
 * with {@link com.haulmont.cuba.core.global.DeletePolicy} DENY value
 */
public class DeletePolicyException extends RuntimeException
{
    private static final long serialVersionUID = -1359432367630173077L;

    private String refEntity;

    public DeletePolicyException(String refEntity) {
        super("Unable to delete: there are references from " + refEntity);
        this.refEntity = refEntity;
    }

    public String getRefEntity() {
        return refEntity;
    }
}
