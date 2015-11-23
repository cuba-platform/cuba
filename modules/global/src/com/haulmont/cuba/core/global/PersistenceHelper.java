/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.global;

import com.haulmont.bali.util.Preconditions;
import com.haulmont.cuba.core.entity.BaseGenericIdEntity;
import com.haulmont.cuba.core.entity.SoftDelete;
import org.apache.commons.lang.StringUtils;

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
     *
     * @param entity entity instance
     * @return <li>true if the instance is new or if it is not a persistent entity, or if it is actually in Managed state
     * but newly-persisted in this transaction
     * <li>false if it is not new Managed or Detached
     * @throws IllegalArgumentException if entity instance is null
     */
    public static boolean isNew(Object entity) {
        Preconditions.checkNotNullArgument(entity, "entity is null");
        if (entity instanceof BaseGenericIdEntity && !((BaseGenericIdEntity) entity).__new()) {
            return false;
        }
//        if (entity instanceof PersistenceCapable) {
//            return ((PersistenceCapable) entity).pcGetStateManager() == null
//                    || ((PersistenceCapable) entity).pcGetStateManager().isNew();
//        }
        return true;
    }

    /**
     * Determines whether the instance is <em>Managed</em>, i.e. attached to a persistence context.
     *
     * @param entity entity instance
     * @return <li>true if the instance is managed,
     * <li>false if it is New (and not yet persisted) or Detached, or if it is not a persistent entity
     * @throws IllegalArgumentException if entity instance is null
     */
    public static boolean isManaged(Object entity) {
        Preconditions.checkNotNullArgument(entity, "entity is null");
        if (entity instanceof BaseGenericIdEntity) {
            return ((BaseGenericIdEntity) entity).__managed();
        }
//        if (entity instanceof PersistenceCapable) {
//            return ((PersistenceCapable) entity).pcGetStateManager() != null
//                    && !((PersistenceCapable) entity).pcGetStateManager().isDetached();
//        }
        return false;
    }

    /**
     * Determines whether the instance is <em>Detached</em>, i.e. stored in database but not attached to a persistence
     * context at the moment.
     *
     * @param entity entity instance
     * @return <li>true if the instance is detached,
     * <li>false if it is New or Managed, or if it is not a persistent entity
     * @throws IllegalArgumentException if entity instance is null
     */
    public static boolean isDetached(Object entity) {
        Preconditions.checkNotNullArgument(entity, "entity is null");
        if (entity instanceof BaseGenericIdEntity && ((BaseGenericIdEntity) entity).__detached()) {
            return true;
        }
//        if (entity instanceof PersistenceCapable) {
//            return ((PersistenceCapable) entity).pcGetStateManager() != null
//                    && ((PersistenceCapable) entity).pcGetStateManager().isDetached();
//        }
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
}
