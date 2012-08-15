/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.chile.core.model;

import javax.annotation.Nullable;
import java.lang.reflect.AnnotatedElement;

/**
 * Metadata object representing an entity attribute.
 *
 * @author abramov
 * @version $Id$
 */
public interface MetaProperty extends MetadataObject<MetaProperty> {

    /**
     * Property type
     */
    enum Type {

        /** Simple type, e.g. String, Number */
        DATATYPE,

        /** Enumeration */
        ENUM,

        /** Reference type. Attribute of this type contains related entity. */
        ASSOCIATION,

        /**
         * Reference type. Attribute of this type contains a related entity.
         * Composition implyes ownership, that is the referenced object exists only as part of the owning entity.
         */
        COMPOSITION
    }

    /**
     * Containing MetaModel
     */
    MetaModel getModel();

    /**
     * MetaClass, containing this MetaProperty
     */
    MetaClass getDomain();

    /**
     * Range of this property.
     */
    Range getRange();

    /**
     * Property type
     */
    Type getType();

    /**
     * Is corresponding entity attribute must contain a value?
     */
    boolean isMandatory();

    /**
     * Is corresponding entity attribute read-only?
     */
    boolean isReadOnly();

    /**
     * Returns a MetaProperty from the opposite side of relation, or null if this is not a reference property
     */
    MetaProperty getInverse();

    /**
     * Returns corresponding Java field or method as AnnotatedElement object
     */
    AnnotatedElement getAnnotatedElement();

    /**
     * Returns Java class of the corresponding field or method's return type
     */
    Class<?> getJavaType();

    /**
     * Returns Java class which declares the corresponding field or method
     */
    @Nullable
    Class<?> getDeclaringClass();
}
