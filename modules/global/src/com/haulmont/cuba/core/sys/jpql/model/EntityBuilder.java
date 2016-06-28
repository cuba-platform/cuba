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

package com.haulmont.cuba.core.sys.jpql.model;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.MessageTools;

public class EntityBuilder {
    private EntityImpl result;

    public EntityImpl produceImmediately(String entityName) {
        return new EntityImpl(entityName);
    }

    public void startNewEntity(MetaClass metaClass) {
        result = new EntityImpl(metaClass.getName());
        result.setUserFriendlyName(AppBeans.get(MessageTools.class).getEntityCaption(metaClass));
    }

    public Entity produceImmediately(String entityName, String... stringAttributeNames) {
        EntityImpl result = new EntityImpl(entityName);
        for (String stringAttributeName : stringAttributeNames) {
            result.addSingleValueAttribute(String.class, stringAttributeName);
        }
        return result;
    }

    public void startNewEntity(String name) {
        result = new EntityImpl(name);
    }

    public void addStringAttribute(String name) {
        addSingleValueAttribute(String.class, name);
    }

    public void addSingleValueAttribute(Class clazz, String name) {
        result.addSingleValueAttribute(clazz, name);
    }

    public void addSingleValueAttribute(Class clazz, String name, String userFriendlyName) {
        result.addSingleValueAttribute(clazz, name, userFriendlyName);
    }

    public void addReferenceAttribute(String name, String referencedEntityName) {
        result.addReferenceAttribute(referencedEntityName, name);
    }

    public void addReferenceAttribute(String name, String referencedEntityName, String userFriendlyName, boolean isEmbedded) {
        result.addReferenceAttribute(referencedEntityName, name ,userFriendlyName, isEmbedded);
    }

    public void addCollectionReferenceAttribute(String name, String referencedEntityName) {
        result.addCollectionReferenceAttribute(referencedEntityName, name);
    }

    public void addCollectionReferenceAttribute(String name, String referencedEntityName, String userFriendlyName) {
        result.addCollectionReferenceAttribute(referencedEntityName, name, userFriendlyName);
    }

    public Entity produce() {
        EntityImpl returnedEntity = result;
        result = null;
        return returnedEntity;
    }
}
