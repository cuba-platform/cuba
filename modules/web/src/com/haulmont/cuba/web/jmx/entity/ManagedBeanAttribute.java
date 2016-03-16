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
import com.haulmont.cuba.core.entity.annotation.SystemLevel;

@MetaClass(name = "jmxcontrol$ManagedBeanAttribute")
@SystemLevel
public class ManagedBeanAttribute extends AbstractNotPersistentEntity {
    private static final long serialVersionUID = 1513762195305899325L;

    @MetaProperty
    private String name;

    @MetaProperty
    private String type;

    @MetaProperty
    private String readableWriteable;

    @MetaProperty
    private Boolean readable;

    @MetaProperty
    private Boolean writeable;

    private Object value;

    private ManagedBeanInfo mbean;

    @MetaProperty
    public String getValueString() {
        return AttributeHelper.convertToString(value);
    }

    public ManagedBeanInfo getMbean() {
        return mbean;
    }

    public void setMbean(ManagedBeanInfo mbean) {
        this.mbean = mbean;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getReadableWriteable() {
        return readableWriteable;
    }

    public void setReadableWriteable(String readableWriteable) {
        this.readableWriteable = readableWriteable;
    }

    public Boolean getReadable() {
        return readable;
    }

    public void setReadable(Boolean readable) {
        this.readable = readable;
    }

    public Boolean getWriteable() {
        return writeable;
    }

    public void setWriteable(Boolean writeable) {
        this.writeable = writeable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
