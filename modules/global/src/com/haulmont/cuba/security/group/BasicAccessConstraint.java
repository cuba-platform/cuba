/*
 * Copyright (c) 2008-2019 Haulmont.
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
 */

package com.haulmont.cuba.security.group;

import com.google.common.base.Strings;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.security.entity.EntityOp;

import java.io.Serializable;
import java.util.function.Predicate;

public class BasicAccessConstraint implements AccessConstraint, Serializable {
    private static final long serialVersionUID = -7454318989839542865L;

    protected String entityType;
    protected EntityOp operation;
    protected String code;
    protected boolean inMemory;
    protected transient Predicate predicate;

    @Override
    public String getEntityType() {
        return entityType;
    }

    @Override
    public EntityOp getOperation() {
        return operation;
    }

    @Override
    public Predicate<? extends Entity> getPredicate() {
        return predicate;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public void setOperation(EntityOp operation) {
        this.operation = operation;
    }

    public void setPredicate(Predicate predicate) {
        this.predicate = predicate;
        this.inMemory = predicate != null;
    }

    @Override
    public boolean isInMemory() {
        return inMemory;
    }

    @Override
    public boolean isCustom() {
        return !Strings.isNullOrEmpty(code);
    }

    @Override
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
