/*
 * Copyright (c) 2008-2018 Haulmont.
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

package com.haulmont.cuba.gui.model.impl;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.cuba.core.app.keyvalue.KeyValueMetaClass;
import com.haulmont.cuba.core.app.keyvalue.KeyValueMetaProperty;
import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.gui.model.KeyValueCollectionContainer;
import com.haulmont.cuba.gui.model.KeyValueContainer;

import javax.annotation.Nullable;
import java.util.Collection;

public class KeyValueCollectionContainerImpl
        extends CollectionContainerImpl<KeyValueEntity> implements KeyValueCollectionContainer {

    private String idName;

    public KeyValueCollectionContainerImpl() {
        super(new KeyValueMetaClass());
    }

    @Override
    public KeyValueMetaClass getEntityMetaClass() {
        return (KeyValueMetaClass) super.getEntityMetaClass();
    }

    @Override
    public KeyValueContainer setIdName(String name) {
        idName = name;
        return this;
    }

    @Override
    public String getIdName() {
        return idName;
    }

    @Override
    public KeyValueContainer addProperty(String name) {
        getEntityMetaClass().addProperty(new KeyValueMetaProperty(getEntityMetaClass(), name, String.class));
        return this;
    }

    @Override
    public KeyValueContainer addProperty(String name, Class aClass) {
        getEntityMetaClass().addProperty(new KeyValueMetaProperty(getEntityMetaClass(), name, aClass));
        return this;
    }

    @Override
    public KeyValueContainer addProperty(String name, Datatype datatype) {
        getEntityMetaClass().addProperty(new KeyValueMetaProperty(getEntityMetaClass(), name, datatype));
        return this;
    }

    @Override
    public void setItems(@Nullable Collection<KeyValueEntity> entities) {
        for (KeyValueEntity entity : entities) {
            entity.setMetaClass(entityMetaClass);
        }
        super.setItems(entities);
    }

    @Override
    public String toString() {
        return "KeyValueCollectionContainerImpl{" +
                "size=" + collection.size() +
                '}';
    }
}
