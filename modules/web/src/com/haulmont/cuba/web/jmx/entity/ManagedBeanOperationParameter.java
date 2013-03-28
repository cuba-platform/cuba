/*
 * Copyright (c) 2008-2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 *
 * Author: Alexander Budarov
 * Created: 24.08.2010 16:15:06
 * $Id$
 */

package com.haulmont.cuba.web.jmx.entity;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.AbstractNotPersistentEntity;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;

@MetaClass(name = "jmxcontrol$ManagedBeanOperationParameter")
@SystemLevel
public class ManagedBeanOperationParameter extends AbstractNotPersistentEntity {
    private static final long serialVersionUID = 4327221019269447414L;

    @MetaProperty
    private String name;

    @MetaProperty
    private String description;

    @MetaProperty
    private String type;

    private ManagedBeanOperation operation;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ManagedBeanOperation getOperation() {
        return operation;
    }

    public void setOperation(ManagedBeanOperation operation) {
        this.operation = operation;
    }
}
