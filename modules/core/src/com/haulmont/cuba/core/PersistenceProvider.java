/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 01.11.2008 13:23:09
 * $Id$
 */
package com.haulmont.cuba.core;

import com.haulmont.cuba.core.entity.BaseEntity;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.sys.ManagedPersistenceProvider;
import org.apache.commons.lang.StringUtils;
import org.apache.openjpa.enhance.PersistenceCapable;
import org.apache.openjpa.kernel.OpenJPAStateManager;
import org.apache.openjpa.meta.FieldMetaData;

import java.lang.annotation.Annotation;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class PersistenceProvider
{
    private static PersistenceProvider instance;

    public static final String PERSISTENCE_XML = "cuba.PersistenceXml";
    public static final String PERSISTENCE_UNIT = "cuba.PersistenceUnit";

    protected static final String DEFAULT_PERSISTENCE_XML = "META-INF/cuba-persistence.xml";
    protected static final String DEFAULT_PERSISTENCE_UNIT = "cuba";

    private static PersistenceProvider getInstance() {
        if (instance == null) {
            instance = new ManagedPersistenceProvider(Locator.getJndiContext());
        }
        return instance;
    }

    public static String getPersistenceXmlPath() {
        String xmlPath = System.getProperty(PERSISTENCE_XML);
        if (StringUtils.isBlank(xmlPath))
            xmlPath = DEFAULT_PERSISTENCE_XML;
        return xmlPath;
    }

    public static String getPersistenceUnitName() {
        String unitName = System.getProperty(PERSISTENCE_UNIT);
        if (StringUtils.isBlank(unitName))
            unitName = DEFAULT_PERSISTENCE_UNIT;
        return unitName;
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        return getInstance().__getEntityManagerFactory();
    }

    public static EntityManager getEntityManager() {
        return getInstance().__getEntityManager();
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

    public static Set<String> getDirtyFields(BaseEntity entity) {
        if (!(entity instanceof PersistenceCapable))
            return Collections.emptySet();

        OpenJPAStateManager stateManager = (OpenJPAStateManager) ((PersistenceCapable) entity).pcGetStateManager();
        if (stateManager == null)
            return Collections.emptySet();

        Set<String> set = new HashSet<String>();
        BitSet dirtySet = stateManager.getDirty();
        for (int i = 0; i < dirtySet.size()-1; i++) {
            if (dirtySet.get(i)) {
                FieldMetaData field = stateManager.getMetaData().getField(i);
                set.add(field.getName());
            }
        }
        return set;
    }

//    public static boolean isDetached(Entity entity) {
//        if (entity instanceof PersistenceCapable)
//            return ((PersistenceCapable) entity).pcIsDetached();
//        else
//            throw new RuntimeException("Entity is not PersistenceCapable: " + entity);
//    }

    protected abstract EntityManagerFactory __getEntityManagerFactory();

    protected abstract EntityManager __getEntityManager();
}
