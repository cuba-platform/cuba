/*
 * Copyright (c) 2008-2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 *
 * Author: Alexander Budarov
 * Created: 19.08.2010 13:27:00
 * $Id$
 */

package com.haulmont.cuba.jmxcontrol.entity;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.AbstractNotPersistentEntity;

import java.lang.reflect.Array;

@MetaClass(name = "jmxcontrol$ManagedBeanAttribute")
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
        if (value == null) {
            return null;
        }

        if (value.getClass().isArray()) {
            StringBuilder b = new StringBuilder();
            for (int i = 0; i < Array.getLength(value); i++) {
                Object o = Array.get(value, i);
                b.append(String.valueOf(o)).append("\n");                
            }
            return b.toString();
        }
        return value.toString();
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
