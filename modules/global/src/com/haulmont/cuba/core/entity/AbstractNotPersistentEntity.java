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
package com.haulmont.cuba.core.entity;

import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.impl.AbstractInstance;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.UuidProvider;
import com.haulmont.cuba.core.sys.CubaEnhanced;
import com.haulmont.cuba.core.sys.CubaEnhancingDisabled;
import org.apache.commons.lang.ObjectUtils;

import java.util.UUID;

/**
 * Base class for not persistent entities.
 *
 */
@com.haulmont.chile.core.annotations.MetaClass(name = "AbstractNotPersistentEntity")
public abstract class AbstractNotPersistentEntity
        extends AbstractInstance
        implements Entity<UUID>, HasUuid, CubaEnhancingDisabled {

    private static final long serialVersionUID = -2846020822531467401L;

    protected UUID id;

    protected boolean __new = true;

    protected AbstractNotPersistentEntity() {
        id = UuidProvider.createUuid();
    }

    @Override
    public UUID getUuid() {
        return id;
    }

    @Override
    public void setUuid(UUID uuid) {
        id = uuid;
    }

    @Override
    public MetaClass getMetaClass() {
        Metadata metadata = AppBeans.get(Metadata.NAME);
        return metadata.getSession().getClassNN(getClass());
    }

    @MetaProperty
    @Override
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public void setValue(String property, Object obj, boolean checkEquals) {
        Object oldValue = getValue(property);
        if ((!checkEquals) || (!ObjectUtils.equals(oldValue, obj))) {
            getMethodsCache().invokeSetter(this, property, obj);
            if (!(this instanceof CubaEnhanced)) {
                propertyChanged(property, oldValue, obj);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AbstractNotPersistentEntity that = (AbstractNotPersistentEntity) o;

        return !(getUuid() != null ? !getUuid().equals(that.getUuid()) : that.getUuid() != null);
    }

    @Override
    public int hashCode() {
        return getUuid() != null ? getUuid().hashCode() : 0;
    }

    @Override
    public String toString() {
        return getClass().getName() + "-" + getUuid();
    }
}
