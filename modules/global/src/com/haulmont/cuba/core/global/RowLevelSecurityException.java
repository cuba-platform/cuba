/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.haulmont.cuba.core.global;

import com.haulmont.cuba.security.entity.ConstraintOperationType;

/**
 * Exception that is raised on different exceptions related to Row Level Security.
 * <p>
 *
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

    public String getEntity() {
        return entity;
    }

    public ConstraintOperationType getOperationType() {
        return operationType;
    }
}
