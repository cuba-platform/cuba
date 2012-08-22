/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.global;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.Session;

/**
 * Central interface to provide metadata-related functionality.
 *
 * @author krivopustov
 * @version $Id$
 */
public interface Metadata {

    String NAME = "cuba_Metadata";

    /**
     * Get Metadata session - interface providing access to MetaClasses and MetaProperties.
     * @return  current metadata session
     */
    Session getSession();

    /**
     * Access to {@link ViewRepository}. It is not a bean and can only be obtained through this method.
     * @return  ViewRepository instance
     */
    ViewRepository getViewRepository();

    /**
     * Convenient access to {@link ExtendedEntities} bean.
     * @return ExtendedEntities instance
     */
    ExtendedEntities getExtendedEntities();

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
    <T> T create(MetaClass metaClass);

    /**
     * Instantiate an entity, taking into account extended entities.
     * @param entityName    entity name
     * @return              entity instance
     */
    <T> T create(String entityName);
}
