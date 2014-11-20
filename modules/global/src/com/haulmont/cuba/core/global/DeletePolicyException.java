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
 * @author krivopustov
 * @version $Id$
 */
public class DeletePolicyException extends RuntimeException {

    private static final long serialVersionUID = -1359432367630173077L;

    private String entity;
    private String refEntity;

    public static final String ERR_MESSAGE = "Unable to delete %s because there are references from %s";

    public DeletePolicyException(String entity, String refEntity) {
        super(String.format(ERR_MESSAGE, entity, refEntity));
        this.entity = entity;
        this.refEntity = refEntity;
    }

    public String getEntity() {
        return entity;
    }

    public String getRefEntity() {
        return refEntity;
    }
}
