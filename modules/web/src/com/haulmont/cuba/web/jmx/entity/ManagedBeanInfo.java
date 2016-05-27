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

package com.haulmont.cuba.web.jmx.entity;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.AbstractNotPersistentEntity;
import com.haulmont.cuba.core.entity.JmxInstance;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;

import java.util.List;

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