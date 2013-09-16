/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.chile.core.model;

import javax.annotation.Nullable;
import java.util.Collection;

/**
 * Metadata model entry point.
 * <p/>Metadata consists of a set of interrelated {@link MetaClass} instances.
 * <p/>The whole metadata is split into named models. One can obtain {@link MetaClass} instances through the
 * {@link MetaModel} references or directly from the Session.
 *
 * @author abramov
 * @version $Id$
 */
public interface Session {

    MetaModel getModel(String name);

    Collection<MetaModel> getModels();

    /**
     * Search MetaClass by its name in the whole metamodel.
     * @param name  entity name
     * @return      MetaClass instance or null if not found
     */
    @Nullable
    MetaClass getClass(String name);

    /**
     * Search MetaClass by its name in the whole metamodel.
     * @param name  entity name
     * @return      MetaClass instance. Throws exception if not found.
     */
    MetaClass getClassNN(String name);

    /**
     * Search MetaClass by the corresponding Java class in the whole metamodel.
     * @param clazz Java class defining the entity
     * @return      MetaClass instance or null if not found
     */
    @Nullable
    MetaClass getClass(Class<?> clazz);

    /**
     * Search MetaClass by the corresponding Java class in the whole metamodel.
     * @param clazz Java class defining the entity
     * @return      MetaClass instance. Throws exception if not found.
     */
    MetaClass getClassNN(Class<?> clazz);

    /**
     * @return collection of all MetaClasses in the whole metamodel
     */
    Collection<MetaClass> getClasses();
}
