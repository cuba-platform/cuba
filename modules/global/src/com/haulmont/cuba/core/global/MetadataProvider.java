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
        return AppBeans.get(Metadata.NAME, Metadata.class);
    }

    /**
     * Get Metadata session - interface providing access to MetaClasses and MetaProperties.
     * @return  current metadata session
     */
    public static Session getSession() {
        return getMetadata().getSession();
    }

    /**
     * Access to {@link ViewRepository}. It is not a bean and can only be obtained through this method.
     * @return  ViewRepository instance
     */
    public static ViewRepository getViewRepository() {
        return getMetadata().getViewRepository();
    }

    /**
     * Convenient access to {@link ExtendedEntities} bean.
     * @return ExtendedEntities instance
     */
    public static ExtendedEntities getExtendedEntities() {
        return getMetadata().getExtendedEntities();
    }

    /**
     * Instantiate an entity, taking into account extended entities.
     * @param entityClass   entity class
     * @return              entity instance
     */
    public static <T> T create(Class<T> entityClass) {
        return (T) getMetadata().create(entityClass);
    }

    /**
     * Instantiate an entity, taking into account extended entities.
     * @param metaClass     entity MetaClass
     * @return              entity instance
     */
    public static <T> T create(MetaClass metaClass) {
        return (T) getMetadata().create(metaClass.getJavaClass());
    }

    /**
     * Instantiate an entity, taking into account extended entities.
     * @param entityName    entity name
     * @return              entity instance
     */
    public static <T> T create(String entityName) {
        return (T) getMetadata().create(entityName);
    }
}
