/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.global;

/**
 * Exception that is raised on attempt to soft delete an object,
 * which has linked objects marked with {@link com.haulmont.cuba.core.entity.annotation.OnDelete} annotation
 * with {@link com.haulmont.cuba.core.global.DeletePolicy#DENY} value.
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class DeletePolicyException extends RuntimeException
{
    private static final long serialVersionUID = -1359432367630173077L;

    private String refEntity;

    public static final String ERR_MESSAGE = "Unable to delete: there are references from ";

    public DeletePolicyException(String refEntity) {
        super(ERR_MESSAGE + refEntity);
        this.refEntity = refEntity;
    }

    public String getRefEntity() {
        return refEntity;
    }
}
