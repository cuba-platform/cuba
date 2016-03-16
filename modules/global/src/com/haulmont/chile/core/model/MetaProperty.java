/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.chile.core.model;

import javax.annotation.Nullable;
import java.lang.reflect.AnnotatedElement;

/**
 * Metadata object representing an entity attribute.
 *
 */
public interface MetaProperty extends MetadataObject {

    /**
     * Property type
     */
    enum Type {

        /** Simple value type, e.g. String, Number */
        DATATYPE,

        /** Enumeration */
        ENUM,

        /** Reference type. Attribute of this type contains a related entity. */
        ASSOCIATION,

        /**
         * Reference type. Attribute of this type contains a related entity.
         * Composition implies ownership, that is the referenced object exists only as part of the owning entity.
         */
        COMPOSITION
    }

    /**
     * @return containing MetaModel instance. Null signifies a temporary metaclass, not associated with an entity class.
     */
    @Nullable
    MetaModel getModel();

    /**
     * @return MetaClass, containing this MetaProperty. <br/>
     * In case of {@link com.haulmont.cuba.core.entity.annotation.Extends} returns extended meta class.
     */
    MetaClass getDomain();

    /**
     * @return Range of this property.
     */
    Range getRange();

    /**
     * @return property type (DATATYPE, ENUM, ASSOCIATION, COMPOSITION)
     */
    Type getType();

    /**
     * @return true if the corresponding entity attribute must contain a value
     */
    boolean isMandatory();

    /**
     * @return true if the corresponding entity attribute is read-only
     */
    boolean isReadOnly();

    /**
     * @return a MetaProperty from the opposite side of relation, or null if this is not a reference property
     */
    @Nullable
    MetaProperty getInverse();

    /**
     * @return corresponding Java field or method as AnnotatedElement object
     */
    AnnotatedElement getAnnotatedElement();

    /**
     * @return Java class of the corresponding field or method's return type
     */
    Class<?> getJavaType();

    /**
     * @return Java class which declares the corresponding field or method
     */
    @Nullable
    Class<?> getDeclaringClass();
}