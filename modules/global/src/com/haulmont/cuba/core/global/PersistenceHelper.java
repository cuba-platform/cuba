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
package com.haulmont.cuba.core.global;

import com.haulmont.cuba.core.entity.BaseGenericIdEntity;

/**
 * Utility class providing some information about persistent entities.
 *
 */
public class PersistenceHelper {

    //this instance is required for unit tests where application context can be not created
    private static EntityStates _entityStates = new EntityStates();

    /**
     * @see EntityStates#isNew(Object)
     */
    public static boolean isNew(Object entity) {
        EntityStates entityStates = AppBeans.get(EntityStates.class);
        return entityStates != null ? entityStates.isNew(entity) : _entityStates.isNew(entity);
    }

    /**
     * @see EntityStates#isManaged(Object)
     */
    public static boolean isManaged(Object entity) {
        EntityStates entityStates = AppBeans.get(EntityStates.class);
        return entityStates != null ? entityStates.isManaged(entity) : _entityStates.isManaged(entity);
    }

    /**
     * @see EntityStates#isDeleted(Object)
     */
    public static boolean isDetached(Object entity) {
        EntityStates entityStates = AppBeans.get(EntityStates.class);
        return entityStates != null ? entityStates.isDetached(entity) : _entityStates.isDetached(entity);
    }

    /**
     * @see EntityStates#getEntityName(Class)
     */
    public static String getEntityName(Class<?> entityClass) {
        EntityStates entityStates = AppBeans.get(EntityStates.class);
        return entityStates != null ? entityStates.getEntityName(entityClass) : _entityStates.getEntityName(entityClass);
    }

    /**
     * @see EntityStates#isSoftDeleted(Class)
     */
    public static boolean isSoftDeleted(Class entityClass) {
        EntityStates entityStates = AppBeans.get(EntityStates.class);
        return entityStates != null ? entityStates.isSoftDeleted(entityClass) : _entityStates.isSoftDeleted(entityClass);
    }

    /**
     * @see EntityStates#isLoaded(Object, String)
     */
    public static boolean isLoaded(Object entity, String property) {
        EntityStates entityStates = AppBeans.get(EntityStates.class);
        return entityStates != null ? entityStates.isLoaded(entity, property) : _entityStates.isLoaded(entity, property);
    }

    /**
     * @see EntityStates#checkLoaded(Object, String...)
     */
    public static void checkLoaded(Object entity, String... properties) {
        EntityStates entityStates = AppBeans.get(EntityStates.class);
        if (entityStates != null) {
            entityStates.checkLoaded(entity, properties);
        } else {
            _entityStates.checkLoaded(entity, properties);
        }
    }

    /**
     * @see EntityStates#isDeleted(Object)
     */
    public static boolean isDeleted(Object entity) {
        EntityStates entityStates = AppBeans.get(EntityStates.class);
        return entityStates != null ? entityStates.isDeleted(entity) : _entityStates.isDeleted(entity);
    }

    /**
     * @see EntityStates#makeDetached(BaseGenericIdEntity)
     */
    public static void makeDetached(BaseGenericIdEntity entity) {
        EntityStates entityStates = AppBeans.get(EntityStates.class);
        if (entityStates != null) {
            entityStates.makeDetached(entity);
        } else {
            _entityStates.makeDetached(entity);
        }
    }

    /**
     * @see EntityStates#makePatch(BaseGenericIdEntity)
     */
    public static void makePatch(BaseGenericIdEntity entity) {
        EntityStates entityStates = AppBeans.get(EntityStates.class);
        if (entityStates != null) {
            entityStates.makePatch(entity);
        } else {
            _entityStates.makePatch(entity);
        }
    }
}