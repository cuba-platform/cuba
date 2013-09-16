/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.global;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.SoftDelete;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.openjpa.enhance.PersistenceCapable;

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
     * Determines whether the instance is in <em>New</em> state.
     * @param entity entity instance
     * @return <code>true</code> if new or if the object provided is not persistent or is not an entity
     */
    public static boolean isNew(Object entity) {
        if (entity instanceof PersistenceCapable)
            return ((PersistenceCapable) entity).pcIsDetached() == null;
        else if (entity instanceof Entity)
            return ((Entity) entity).getId() != null;
        else
            return true;
    }

    /**
     * Determines whether the instance is in <em>Detached</em> state.
     * @param entity entity instance
     * @return <code>true</code> if the instance is detached or if it is not a persistent entity
     */
    public static boolean isDetached(Object entity) {
        return !(entity instanceof PersistenceCapable)
                || BooleanUtils.isTrue(((PersistenceCapable) entity).pcIsDetached());
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
     */
    @Nullable
    public static String getTableName(Class<?> entityClass) {
        Table annotation = entityClass.getAnnotation(Table.class);
        return annotation == null ? null : annotation.name();
    }
}
