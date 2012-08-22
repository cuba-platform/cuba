/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.chile.core.model;

import javax.annotation.Nullable;
import java.util.Collection;

/**
 * Metadata object representing an entity.
 *
 * @author abramov
 * @version $Id$
 */
public interface MetaClass extends MetadataObject<MetaClass> {

    /**
     * @return containing MetaModel instance
     */
    MetaModel getModel();

    /**
     * @return corresponding Java class
     */
    Class getJavaClass();

    /**
     * Get MetaProperty by its name.
     * @return MetaProperty instance, or null if no such property found
     */
    @Nullable
    MetaProperty getProperty(String name);

    /**
     * DEPRECATED - use {@link #getPropertyPath(String)} instead
     * Returns MetaPropertyPath object, representing path to the property from the current class
     * @param propertyPath dot-separated string
     * @return MetaPropertyPath instance. If the input parameter is wrong, an instance of
     * MetaPropertyPath will be returned anyway.
     */
    @Deprecated
    MetaPropertyPath getPropertyEx(String propertyPath);

    /**
     * Returns MetaPropertyPath object, representing path to the property from the current class
     * @param propertyPath dot-separated string
     * @return MetaPropertyPath instance, or null if the input parameter doesn't represent a valid path.
     */
    MetaPropertyPath getPropertyPath(String propertyPath);

    /**
     * @return collection of meta properties directly owned by this metaclass.
     */
    Collection<MetaProperty> getOwnProperties();

    /**
     * @return collection of meta properties owned by this metaclass and all its ancestors.
     */
    Collection<MetaProperty> getProperties();

    /**
     * Create an instance of the corresponding Java class.
     */
    <T> T createInstance() throws InstantiationException, IllegalAccessException;
}
