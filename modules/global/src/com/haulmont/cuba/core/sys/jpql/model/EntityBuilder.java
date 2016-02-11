/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.jpql.model;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.MessageTools;

/**
 * User: Alex Chevelev
 * Date: 13.10.2010
 * Time: 20:41:42
 */
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
