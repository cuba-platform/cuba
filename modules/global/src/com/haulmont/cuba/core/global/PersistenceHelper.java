/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.global;

import com.haulmont.bali.util.Preconditions;
import com.haulmont.cuba.core.entity.BaseEntity;
import com.haulmont.cuba.core.entity.SoftDelete;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.openjpa.enhance.PersistenceCapable;
import org.apache.openjpa.kernel.StateManagerImpl;

import javax.annotation.Nullable;
import javax.persistence.Table;
import java.lang.annotation.Annotation;

/**
 * Utility class providing some information about persistent entities.
 *
 * @author abramov
 * @version $Id$
 */
public class PersistenceHelper {

    /**
     * Determines whether the instance is <em>New</em>, i.e. just created and not stored in database yet.
     * @param entity entity instance
     * @return <li>true if the instance is new or if it is not a persistent entity, or if it is actually in Managed state
     *              but newly-persisted in this transaction
     *         <li>false if it is not new Managed or Detached
     * @throws IllegalArgumentException if entity instance is null
     */
    public static boolean isNew(Object entity) {
        Preconditions.checkNotNullArgument(entity, "entity is null");
        if (entity instanceof BaseEntity && ((BaseEntity) entity).isDetached()) {
            return false;
        }
        if (entity instanceof PersistenceCapable) {
            return ((PersistenceCapable) entity).pcGetStateManager() == null
                    || ((PersistenceCapable) entity).pcGetStateManager().isNew();
        }
        return true;
    }

    /**
     * Determines whether the instance is <em>Managed</em>, i.e. attached to a persistence context.
     * @param entity entity instance
     * @return <li>true if the instance is managed,
     *         <li>false if it is New (and not yet persisted) or Detached, or if it is not a persistent entity
     * @throws IllegalArgumentException if entity instance is null
     */
    public static boolean isManaged(Object entity) {
        Preconditions.checkNotNullArgument(entity, "entity is null");
        if (entity instanceof PersistenceCapable) {
            return ((PersistenceCapable) entity).pcGetStateManager() != null
                    && !((PersistenceCapable) entity).pcGetStateManager().isDetached();
        }
        return false;
    }

    /**
     * Determines whether the instance is <em>Detached</em>, i.e. stored in database but not attached to a persistence
     * context at the moment.
     * @param entity entity instance
     * @return <li>true if the instance is detached,
     *         <li>false if it is New or Managed, or if it is not a persistent entity
     * @throws IllegalArgumentException if entity instance is null
     */
    public static boolean isDetached(Object entity) {
        Preconditions.checkNotNullArgument(entity, "entity is null");
        if (entity instanceof BaseEntity && ((BaseEntity) entity).isDetached()) {
            return true;
        }
        if (entity instanceof PersistenceCapable) {
            return ((PersistenceCapable) entity).pcGetStateManager() != null
                    && ((PersistenceCapable) entity).pcGetStateManager().isDetached();
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
     * @param entityClass entity class
     * @return <code>true</code> if the entity implements {@link SoftDelete}
     */
    public static boolean isSoftDeleted(Class entityClass) {
        return SoftDelete.class.isAssignableFrom(entityClass);
    }

    /**
     * @param entityClass entity class
     * @return table name as defined in {@link Table} annotation, or <code>null</code> if there is no such annotation
     * @deprecated please use com.haulmont.cuba.core.global.MetadataTools#getDatabaseTable instead
     */
    @Deprecated
    @Nullable
    public static String getTableName(Class<?> entityClass) {
        Table annotation = entityClass.getAnnotation(Table.class);
        return annotation == null ? null : annotation.name();
    }
}
