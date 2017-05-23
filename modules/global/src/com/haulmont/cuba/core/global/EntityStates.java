/*
 * Copyright (c) 2008-2017 Haulmont.
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

package com.haulmont.cuba.core.global;

import com.haulmont.bali.util.Preconditions;
import com.haulmont.cuba.core.entity.AbstractNotPersistentEntity;
import com.haulmont.cuba.core.entity.BaseEntityInternalAccess;
import com.haulmont.cuba.core.entity.BaseGenericIdEntity;
import com.haulmont.cuba.core.entity.SoftDelete;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.Objects;

/**
 * Utility class providing some information about entities states.
 */
@Component(EntityStates.NAME)
public class EntityStates {
    public static final String NAME = "cuba_EntityStates";

    /**
     * Determines whether the instance is <em>New</em>, i.e. just created and not stored in database yet.
     *
     * @param entity entity instance
     * @return  - true if the instance is a new persistent entity, or if it is actually in Managed state
     *            but newly-persisted in this transaction <br>
     *          - true if the instance is a new non-persistent entity never returned from DataManager <br>
     *          - false otherwise
     * @throws IllegalArgumentException if entity instance is null
     */
    public boolean isNew(Object entity) {
        Preconditions.checkNotNullArgument(entity, "entity is null");
        if (entity instanceof BaseGenericIdEntity) {
            return BaseEntityInternalAccess.isNew((BaseGenericIdEntity) entity);
        } else if (entity instanceof AbstractNotPersistentEntity) {
            return BaseEntityInternalAccess.isNew((AbstractNotPersistentEntity) entity);
        }
        return false;
    }

    /**
     * Determines whether the instance is <em>Managed</em>, i.e. attached to a persistence context.
     *
     * @param entity entity instance
     * @return - true if the instance is managed,<br>
     *         - false if it is New (and not yet persisted) or Detached, or if it is not a persistent entity
     * @throws IllegalArgumentException if entity instance is null
     */
    public boolean isManaged(Object entity) {
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
     * @return - true if the instance is detached,<br>
     *         - false if it is New or Managed, or if it is not a persistent entity
     * @throws IllegalArgumentException if entity instance is null
     */
    public boolean isDetached(Object entity) {
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
    public String getEntityName(Class<?> entityClass) {
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
     * @return {@code true} if the entity implements {@link SoftDelete}
     */
    public boolean isSoftDeleted(Class entityClass) {
        return SoftDelete.class.isAssignableFrom(entityClass);
    }

    /**
     * Checks if the property is loaded from DB.
     * <p>Non-persistent attributes are considered loaded if they do not have related properties, or all related
     * properties are loaded.
     *
     * @param entity   entity
     * @param property name of the property. Only immediate attributes of the entity are supported.
     * @return true if loaded
     */
    public boolean isLoaded(Object entity, String property) {
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
    public void checkLoaded(Object entity, String... properties) {
        Objects.requireNonNull(entity);

        for (String property: properties) {
            if (!isLoaded(entity, property)) {
                String errorMessage = String.format("%s.%s is not loaded", entity.getClass().getSimpleName(), property);
                throw new IllegalArgumentException(errorMessage);
            }
        }
    }

    /**
     * Determines whether the entity instance was <em>deleted</em>.
     *
     * @param entity entity instance
     * @return - true if the instance was deleted
     *         - false otherwise
     * @throws IllegalArgumentException if entity instance is null
     */
    public boolean isDeleted(Object entity) {
        Preconditions.checkNotNullArgument(entity, "entity is null");
        if (entity instanceof SoftDelete && ((SoftDelete) entity).isDeleted())
            return true;
        if (entity instanceof BaseGenericIdEntity && BaseEntityInternalAccess.isRemoved((BaseGenericIdEntity) entity)) {
            return true;
        }
        return false;
    }

    /**
     * Makes a newly constructed object detached. The detached object can be passed to {@code DataManager.commit()} or
     * to {@code EntityManager.merge()} to save its state to the database.
     * <p>If an object with such ID does not exist in the database, a new object will be inserted.
     * <p>If the entity is {@code Versioned}, the version attribute should be equal to the latest version existing in
     * the database, or null for a new object.
     *
     * @param entity    entity in the New state
     * @throws IllegalStateException if the entity is Managed
     * @see #isDetached(Object)
     * @see #makePatch(BaseGenericIdEntity)
     */
    public void makeDetached(BaseGenericIdEntity entity) {
        Preconditions.checkNotNullArgument(entity, "entity is null");
        if (BaseEntityInternalAccess.isManaged(entity))
            throw new IllegalStateException("entity is managed");

        BaseEntityInternalAccess.setNew(entity, false);
        BaseEntityInternalAccess.setDetached(entity, true);
    }

    /**
     * Makes a newly constructed object a patch object. The patch object is {@code !isNew() && !isDetached() && !isManaged()}.
     * The patch object can be passed to {@code DataManager.commit()} or
     * to {@code EntityManager.merge()} to save its state to the database. Only <b>non-null values</b> of attributes are
     * updated.
     * <p>If an object with such ID does not exist in the database, a new object will be inserted.
     * <p>If the entity is {@code Versioned}, the version attribute should be null or equal to the latest version existing in
     * the database.
     *
     * @param entity    entity in the New or Detached state
     * @throws IllegalStateException if the entity is Managed
     * @see #isDetached(Object)
     * @see #makeDetached(BaseGenericIdEntity)
     */
    public void makePatch(BaseGenericIdEntity entity) {
        Preconditions.checkNotNullArgument(entity, "entity is null");
        if (BaseEntityInternalAccess.isManaged(entity))
            throw new IllegalStateException("entity is managed");

        BaseEntityInternalAccess.setNew(entity, false);
        BaseEntityInternalAccess.setDetached(entity, false);
    }
}