/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.global;

import com.haulmont.cuba.security.entity.ConstraintOperationType;

/**
 * Exception that is raised on different exceptions related to Row Level Security.
 * <p>
 * <p>$Id$</p>
 *
 * @author degtyarjov
 */
@SupportedByClient
@Logging(Logging.Type.BRIEF)
public class RowLevelSecurityException extends RuntimeException {
    private static final long serialVersionUID = -3097861878301424338L;

    private final String entity;
    private final ConstraintOperationType operationType;

    public RowLevelSecurityException(String message, String entity) {
        super(message);
        this.entity = entity;
        this.operationType = null;
    }

    public RowLevelSecurityException(String message, String entity, ConstraintOperationType operationType) {
        super(message);
        this.entity = entity;
        this.operationType = operationType;
    }

    public RowLevelSecurityException(Throwable throwable, String message, String entity) {
        super(message, throwable);
        this.entity = entity;
        this.operationType = null;
    }

    public String getEntity() {
        return entity;
    }

    public ConstraintOperationType getOperationType() {
        return operationType;
    }
}
