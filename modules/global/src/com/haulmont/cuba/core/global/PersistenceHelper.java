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
import com.haulmont.cuba.core.entity.SoftDelete;
import org.apache.openjpa.enhance.PersistenceCapable;
import org.apache.openjpa.kernel.OpenJPAStateManager;
import org.apache.openjpa.meta.ClassMetaData;
import org.apache.openjpa.meta.FieldMetaData;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.ClassUtils;

import java.util.BitSet;
import java.util.List;
import java.lang.annotation.Annotation;

/**
 * Helper providing some information about persistent entities.
 */
public class PersistenceHelper {

    public static boolean isNew(Object entity) {
        if (entity instanceof PersistenceCapable)
            return ((PersistenceCapable) entity).pcIsDetached() == null;
        else if (entity instanceof Entity)
            return ((Entity) entity).getId() != null;
        else
            return true;
    }

    public static boolean isDetached(Object entity) {
        return !(entity instanceof PersistenceCapable)
                || BooleanUtils.isTrue(((PersistenceCapable) entity).pcIsDetached());
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
        if (SoftDelete.class.isAssignableFrom(entityClass)) {
            softDelete = true;
        } else {
            for (Class c : (List<Class>) ClassUtils.getAllSuperclasses(entityClass)) {
                if (SoftDelete.class.isAssignableFrom(c)) {
                    softDelete = true;
                    break;
                }
            }
        }
        return softDelete;
    }
}
