/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.global;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.Session;

import java.util.Map;

/**
 * Central interface to provide metadata functionality
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public interface Metadata {

    String NAME = "cuba_Metadata";

    /**
     * Get Metadata session - interface providing access to MetaClasses and MetaProperties
     * @return  current metadata session
     */
    Session getSession();

    /**
     * Get {@link ViewRepository} singleton instance
     * @return  ViewRepository instance
     */
    ViewRepository getViewRepository();

    /**
     * Get map of entity classes, that are replaced with descendants in an extension project.
     * <p>This map allows the platform to instantiate entity classes, defined in high-level projects, instead of
     * its own entities.</p>
     * <p>Replacing entities may be registered through <em>*-metadata.xml</em> files.</p>
     * @return  map of classes where key is descendant and value is an ascendant class that must be instatiated.
     */
    Map<Class, Class> getReplacedEntities();

    /**
     * Instantiate an entity, taking into account replaced entities - see {@link #getReplacedEntities()}
     * @param entityClass   entity class
     * @return              entity instance
     */
    <T> T create(Class<T> entityClass);

    /**
     * Instantiate an entity, taking into account replaced entities - see {@link #getReplacedEntities()}
     * @param metaClass     entity MetaClass
     * @return              entity instance
     */
    <T> T create(MetaClass metaClass);

    /**
     * Instantiate an entity, taking into account replaced entities - see {@link #getReplacedEntities()}
     * @param entityName    entity name
     * @return              entity instance
     */
    <T> T create(String entityName);

    /**
     * Get replacing ancestor class if exists. See {@link #getReplacedEntities()}.
     * @param entityClass   entity class
     * @return              replacing ancestor class, or the passed class, if no replacement rgistered.
     */
    <T> Class<T> getReplacedClass(Class<T> entityClass);

    /**
     * Get replacing ancestor class if exists. See {@link #getReplacedEntities()}.
     * @param metaClass     entity MetaClass
     * @return              replacing ancestor class, or the passed class, if no replacement rgistered.
     */
    <T> Class<T> getReplacedClass(MetaClass metaClass);

    /**
     * Get replacing ancestor class if exists. See {@link #getReplacedEntities()}.
     * @param entityName    entity name
     * @return              replacing ancestor class, or the passed class, if no replacement rgistered.
     */
    <T> Class<T> getReplacedClass(String entityName);
}
