/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 10.12.2008 11:57:54
 *
 * $Id$
 */
package com.haulmont.cuba.core.global;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.Session;
import com.haulmont.cuba.core.sys.AppContext;

import java.util.Map;

/**
 * Utility class to provide metadata functionality in static context.<br>
 * <p>Injected {@link Metadata} interface should be used instead of this class wherever possible.</p>
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public abstract class MetadataProvider
{
    private static Metadata getMetadata() {
        return AppContext.getBean(Metadata.NAME, Metadata.class);
    }

    /**
     * Get Metadata session - interface providing access to MetaClasses and MetaProperties
     * @return  current metadata session
     */
    public static Session getSession() {
        return getMetadata().getSession();
    }

    /**
     * Get {@link ViewRepository} singleton instance
     * @return  ViewRepository instance
     */
    public static ViewRepository getViewRepository() {
        return getMetadata().getViewRepository();
    }

    /**
     * Get map of entity classes, that are replaced with descendants in an extension project.
     * <p>This map allows the platform to instantiate entity classes, defined in high-level projects, instead of
     * its own entities.</p>
     * <p>Replacing entities may be registered through <em>*-metadata.xml</em> files.</p>
     * @return  map of classes where key is descendant and value is an ascendant class that must be instatiated.
     */
    public static Map<Class, Class> getReplacedEntities() {
        return getMetadata().getReplacedEntities();
    }

    /**
     * Instantiate an entity, taking into account replaced entities - see {@link #getReplacedEntities()}
     * @param entityClass   entity class
     * @return              entity instance
     */
    public static <T> T create(Class<T> entityClass) {
        return (T) getMetadata().create(entityClass);
    }

    /**
     * Instantiate an entity, taking into account replaced entities - see {@link #getReplacedEntities()}
     * @param metaClass     entity MetaClass
     * @return              entity instance
     */
    public static <T> T create(MetaClass metaClass) {
        return (T) getMetadata().create(metaClass.getJavaClass());
    }

    /**
     * Instantiate an entity, taking into account replaced entities - see {@link #getReplacedEntities()}
     * @param entityName    entity name
     * @return              entity instance
     */
    public static <T> T create(String entityName) {
        return (T) getMetadata().create(entityName);
    }

    /**
     * Get replacing ancestor class if exists. See {@link #getReplacedEntities()}.
     * @param entityClass   entity class
     * @return              replacing ancestor class, or the passed class, if no replacement rgistered.
     */
    public static <T> Class<T> getReplacedClass(Class<T> entityClass) {
        return getMetadata().getReplacedClass(entityClass);
    }

    /**
     * Get replacing ancestor class if exists. See {@link #getReplacedEntities()}.
     * @param metaClass     entity MetaClass
     * @return              replacing ancestor class, or the passed class, if no replacement rgistered.
     */
    public static <T> Class<T> getReplacedClass(MetaClass metaClass) {
        return getMetadata().getReplacedClass(metaClass.getJavaClass());
    }

    /**
     * Get replacing ancestor class if exists. See {@link #getReplacedEntities()}.
     * @param entityName    entity name
     * @return              replacing ancestor class, or the passed class, if no replacement rgistered.
     */
    public static <T> Class<T> getReplacedClass(String entityName) {
        return getMetadata().getReplacedClass(entityName);
    }
}
