/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.chile.core.model;

import java.util.Collection;

/**
 * Container and entry point for metadata objects
 *
 * @author abramov
 * @version $Id$
 */
public interface MetaModel extends MetadataObject {

    /**
     * Get MetaClass by its unique name
     * @return MetaClass instance, null if not found
     */
    MetaClass getClass(String name);

    /**
     * Get MetaClass by corresponding entity's Java class
     * @return MetaClass instance, null if not found
     */
    MetaClass getClass(Class<?> clazz);

    /**
     * All metaclasses
     */
    Collection<MetaClass> getClasses();
}
