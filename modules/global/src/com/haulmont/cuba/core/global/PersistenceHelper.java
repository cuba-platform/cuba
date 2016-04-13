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

import com.haulmont.bali.util.Preconditions;
import com.haulmont.cuba.core.entity.BaseEntityInternalAccess;
import com.haulmont.cuba.core.entity.BaseGenericIdEntity;
import com.haulmont.cuba.core.entity.SoftDelete;
import org.apache.commons.lang.StringUtils;

import java.lang.annotation.Annotation;
import java.util.Objects;

/**
 * Utility class providing some information about persistent entities.
 *
 */
public class PersistenceHelper {

    /**
     * Determines whether the instance is <em>New</em>, i.e. just created and not stored in database yet.
     *
     * @param entity entity instance
     * @return  <li>true if the instance is new or if it is not a persistent entity, or if it is actually in Managed state
     *              but newly-persisted in this transaction
     *          <li>false otherwise
     * @throws IllegalArgumentException if entity instance is null
     */
    public static boolean isNew(Object entity) {
        Preconditions.checkNotNullArgument(entity, "entity is null");
        if (entity instanceof BaseGenericIdEntity && !BaseEntityInternalAccess.isNew((BaseGenericIdEntity) entity)) {
            return false;
        }
        return true;
    }

    /**
     * Determines whether the instance is <em>Managed</em>, i.e. attached to a persistence context.
     *
     * @param entity entity instance
     * @return  <li>true if the instance is managed,
     *          <li>false if it is New (and not yet persisted) or Detached, or if it is not a persistent entity
     * @throws IllegalArgumentException if entity instance is null
     */
    public static boolean isManaged(Object entity) {
        Preconditions.checkNotNullArgument(entity, "entity is null");
        if (entity instanceof BaseGenericIdEntity) {
            return BaseEntityInternalAccess.isManaged((BaseGenericIdEntity) entity);
        }
        return false;
    }

    /**
     * Determines whether the instance is <em>Detached</em>, i.e. stored in database but not attached to a persistence
     * context at the moment.
     *
     * @param entity entity instance
     * @return  <li>true if the instance is detached,
     *          <li>false if it is New or Managed, or if it is not a persistent entity
     * @throws IllegalArgumentException if entity instance is null
     */
    public static boolean isDetached(Object entity) {
        Preconditions.checkNotNullArgument(entity, "entity is null");
        if (entity instanceof BaseGenericIdEntity && BaseEntityInternalAccess.isDetached((BaseGenericIdEntity) entity)) {
            return true;
        }
        return false;
    }

    /**
     * @param entityClass entity class
     * @return entity name as defined in {@link javax.persistence.Entity} annotation
     */
    public static String getEntityName(Class<?> entityClass) {
        Annotation annotation = entityClass.getAnnotation(javax.persistence.Entity.class);
        if (annotation == null)
            throw new IllegalArgumentException("Class " + entityClass + " is not a persistent entity");
        String name = ((javax.persistence.Entity) annotation).name();
        if (!StringUtils.isEmpty(name))
            return name;
        else
            return entityClass.getSimpleName();
    }

    /**
     * Determines whether the entity supports <em>Soft Deletion</em>.
     *
     * @param entityClass entity class
     * @return <code>true</code> if the entity implements {@link SoftDelete}
     */
    public static boolean isSoftDeleted(Class entityClass) {
        return SoftDelete.class.isAssignableFrom(entityClass);
    }

    /**
     * Checks if the property is loaded from DB.
     *
     * @param entity   entity
     * @param property name of the property. Only immediate attributes of the entity are supported.
     * @return true if loaded
     */
    public static boolean isLoaded(Object entity, String property) {
        PersistentAttributesLoadChecker checker = AppBeans.get(PersistentAttributesLoadChecker.NAME);
        return checker.isLoaded(entity, property);
    }

    /**
     * Check that entity has all specified properties loaded from DB.
     * Throw exception if property is not loaded.
     *
     * @param entity entity
     * @param properties property names
     * @throws IllegalArgumentException if at least one of properties is not loaded
     */
    public static void checkLoaded(Object entity, String... properties) {
        Objects.requireNonNull(entity);

        for (String property: properties) {
            if (!isLoaded(entity, property)) {
                String errorMessage = String.format("%s.%s is not loaded", entity.getClass().getSimpleName(), property);
                throw new IllegalArgumentException(errorMessage);
            }
        }
    }
}
