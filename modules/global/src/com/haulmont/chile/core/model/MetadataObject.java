/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.chile.core.model;

import java.util.Map;
import java.util.UUID;
import java.util.Collection;

/**
 * Ancestor of main metadata objects: {@link com.haulmont.chile.core.model.MetaClass} and {@link com.haulmont.chile.core.model.MetaProperty}
 */
public interface MetadataObject<T extends MetadataObject> {

    /**
     * Immediate ancestor of the object, or null if there is no one
     */
    T getAncestor();

    /**
     * All ancestors of the object, recursively. Order is undefined.
     */
    Collection<T> getAncestors();

    /**
     * All descendants of the object, recursively. Order is undefined.
     */
    Collection<T> getDescendants();

    /**
     * MetadataObject unique name
     */
    String getName();

    /**
     * Not used
     */
    String getFullName();

    /**
     * Localized MetadataObject caption for use in UI
     */
    String getCaption();

    /**
     * Localized MetadataObject description for use in UI
     */
    String getDescription();

    /**
     * Unique ID
     */
    UUID getUUID();

    /**
     * MetadataObject annotations. Annotations here are simply name-value pairs, not correlated with Java annotations.
     */
    Map<String, Object> getAnnotations();
}
