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

    /**
     * @see EntityStates#isNew(Object)
     */
    public static boolean isNew(Object entity) {
        return AppBeans.get(EntityStates.class).isNew(entity);
    }

    /**
     * @see EntityStates#isManaged(Object)
     */
    public static boolean isManaged(Object entity) {
        return AppBeans.get(EntityStates.class).isManaged(entity);
    }

    /**
     * @see EntityStates#isDeleted(Object)
     */
    public static boolean isDetached(Object entity) {
        return AppBeans.get(EntityStates.class).isDetached(entity);
    }

    /**
     * @see EntityStates#getEntityName(Class)
     */
    public static String getEntityName(Class<?> entityClass) {
        return AppBeans.get(EntityStates.class).getEntityName(entityClass);
    }

    /**
     * @see EntityStates#isSoftDeleted(Class)
     */
    public static boolean isSoftDeleted(Class entityClass) {
        return AppBeans.get(EntityStates.class).isSoftDeleted(entityClass);
    }

    /**
     * @see EntityStates#isLoaded(Object, String)
     */
    public static boolean isLoaded(Object entity, String property) {
        return AppBeans.get(EntityStates.class).isLoaded(entity, property);
    }

    /**
     * @see EntityStates#checkLoaded(Object, String...)
     */
    public static void checkLoaded(Object entity, String... properties) {
        AppBeans.get(EntityStates.class).checkLoaded(entity, properties);
    }

    /**
     * @see EntityStates#isDeleted(Object)
     */
    public static boolean isDeleted(Object entity) {
        return AppBeans.get(EntityStates.class).isDetached(entity);
    }

    /**
     * @see EntityStates#makeDetached(BaseGenericIdEntity)
     */
    public static void makeDetached(BaseGenericIdEntity entity) {
        AppBeans.get(EntityStates.class).makeDetached(entity);
    }

    /**
     * @see EntityStates#makePatch(BaseGenericIdEntity)
     */
    public static void makePatch(BaseGenericIdEntity entity) {
        AppBeans.get(EntityStates.class).makePatch(entity);
    }
}