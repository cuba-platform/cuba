/*
 * Copyright (c) 2008-2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.jmx.entity;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.AbstractNotPersistentEntity;
import com.haulmont.cuba.core.entity.JmxInstance;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;

import java.util.List;

/**
 * @author budarov
 * @version $Id$
 */
@MetaClass(name = "jmxcontrol$ManagedBeanInfo")
@SystemLevel
public class ManagedBeanInfo extends AbstractNotPersistentEntity {
    private static final long serialVersionUID = 7397761789851370883L;

    @MetaProperty
    private String className;
    
    @MetaProperty
    private String description;

    @MetaProperty
    private String objectName;

    @MetaProperty
    private String domain;

    @MetaProperty
    private String propertyList;

    @MetaProperty
    private JmxInstance jmxInstance;

    private List<ManagedBeanAttribute> attributes;

    private List<ManagedBeanOperation> operations;

    public List<ManagedBeanOperation> getOperations() {
        return operations;
    }

    public void setOperations(List<ManagedBeanOperation> operations) {
        this.operations = operations;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getPropertyList() {
        return propertyList;
    }

    public void setPropertyList(String propertyList) {
        this.propertyList = propertyList;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public List<ManagedBeanAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<ManagedBeanAttribute> attributes) {
        this.attributes = attributes;
    }

    public JmxInstance getJmxInstance() {
        return jmxInstance;
    }

    public void setJmxInstance(JmxInstance jmxInstance) {
        this.jmxInstance = jmxInstance;
    }
}