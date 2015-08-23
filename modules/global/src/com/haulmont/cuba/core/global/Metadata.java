/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.global;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.Session;
import com.haulmont.cuba.core.entity.Entity;

/**
 * Central interface to provide metadata-related functionality.
 *
 * @author krivopustov
 * @version $Id$
 */
public interface Metadata extends Session {

    String NAME = "cuba_Metadata";

    /**
     * Get Metadata session - interface providing access to MetaClasses and MetaProperties.
     * @return  current metadata session
     */
    Session getSession();

    /**
     * Convenient access to {@link ViewRepository} bean.
     * @return  ViewRepository instance
     */
    ViewRepository getViewRepository();

    /**
     * Convenient access to {@link ExtendedEntities} bean.
     * @return ExtendedEntities instance
     */
    ExtendedEntities getExtendedEntities();

    /**
     * Convenient access to {@link MetadataTools} bean.
     * @return  MetadataTools instance
     */
    MetadataTools getTools();

    /**
     * Instantiate an entity, taking into account extended entities.
     * @param entityClass   entity class
     * @return              entity instance
     */
    <T> T create(Class<T> entityClass);

    /**
     * Instantiate an entity, taking into account extended entities.
     * @param metaClass     entity MetaClass
     * @return              entity instance
     */
    Entity create(MetaClass metaClass);

    /**
     * Instantiate an entity, taking into account extended entities.
     * @param entityName    entity name
     * @return              entity instance
     */
    Entity create(String entityName);
}
