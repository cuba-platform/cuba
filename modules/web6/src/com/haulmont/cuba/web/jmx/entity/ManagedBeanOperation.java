/*
 * Copyright (c) 2008-2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 *
 * Author: Alexander Budarov
 * Created: 24.08.2010 16:12:29
 * $Id$
 */

package com.haulmont.cuba.web.jmx.entity;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.AbstractNotPersistentEntity;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;

import java.util.List;

@MetaClass(name = "jmxcontrol$ManagedBeanOperation")
@SystemLevel
public class ManagedBeanOperation extends AbstractNotPersistentEntity {
    private static final long serialVersionUID = 4932698715958055857L;

    @MetaProperty
    private String name;

    @MetaProperty
    private String returnType;

    @MetaProperty
    private String description;

    private ManagedBeanInfo mbean;

    private List<ManagedBeanOperationParameter> parameters;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ManagedBeanInfo getMbean() {
        return mbean;
    }

    public void setMbean(ManagedBeanInfo mbean) {
        this.mbean = mbean;
    }

    public List<ManagedBeanOperationParameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<ManagedBeanOperationParameter> parameters) {
        this.parameters = parameters;
    }
}
