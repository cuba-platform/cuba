/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 13.02.2009 16:35:29
 * $Id$
 */
package com.haulmont.cuba.core.global;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.DeleteDeferred;
import org.apache.openjpa.enhance.PersistenceCapable;
import org.apache.openjpa.enhance.StateManager;
import org.apache.openjpa.kernel.OpenJPAStateManager;
import org.apache.openjpa.meta.ClassMetaData;
import org.apache.openjpa.meta.FieldMetaData;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.ClassUtils;

import java.util.BitSet;
import java.util.List;
import java.lang.annotation.Annotation;

public class PersistenceHelper {

    public static boolean isNew(Entity entity) {
        if (entity instanceof PersistenceCapable)
            return ((PersistenceCapable) entity).pcIsDetached() == null;
        else
            return entity.getId() != null;
    }

    public static boolean isDetached(Entity entity) {
        if (entity instanceof PersistenceCapable)
            return BooleanUtils.isTrue(((PersistenceCapable) entity).pcIsDetached());
        else
            return true;
    }

    public static boolean isLoaded(Entity entity, String property) {
        if (entity instanceof PersistenceCapable) {
            final PersistenceCapable persistenceCapable = (PersistenceCapable) entity;
            final OpenJPAStateManager stateManager = (OpenJPAStateManager) persistenceCapable.pcGetStateManager();

            final BitSet loaded = stateManager.getLoaded();
            final ClassMetaData metaData = stateManager.getMetaData();

            final FieldMetaData fieldMetaData = metaData.getField(property);
            if (fieldMetaData == null) throw new IllegalStateException();

            final int index = fieldMetaData.getIndex();

            return loaded.get(index);
        } else {
            return true;
        }
    }

    public static String getEntityName(Class entityClass) {
        Annotation annotation = entityClass.getAnnotation(javax.persistence.Entity.class);
        if (annotation == null)
            throw new IllegalArgumentException("Class " + entityClass + " is not an entity");
        String name = ((javax.persistence.Entity) annotation).name();
        if (!StringUtils.isEmpty(name))
            return name;
        else
            return entityClass.getSimpleName();
    }

    public static boolean isSoftDeleted(Class entityClass) {
        boolean softDelete = false;
        if (DeleteDeferred.class.isAssignableFrom(entityClass)) {
            softDelete = true;
        } else {
            for (Class c : (List<Class>) ClassUtils.getAllSuperclasses(entityClass)) {
                if (DeleteDeferred.class.isAssignableFrom(c)) {
                    softDelete = true;
                    break;
                }
            }
        }
        return softDelete;
    }
}
