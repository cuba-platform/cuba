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
import java.util.Collection;
import java.util.List;

/**
 * Metadata object representing an entity.
 *
 */
public interface MetaClass extends MetadataObject {

    /**
     * @return containing MetaModel instance. Null signifies a temporary metaclass, not associated with an entity class.
     *
     */
    @Nullable
    MetaModel getModel();

    /**
     * @return corresponding Java class
     */
    Class getJavaClass();

    /**
     * Immediate ancestor of the object, or null if there is no one.
     */
    @Nullable
    MetaClass getAncestor();

    /**
     * All ancestors of the metaclass, in order going up from the immediate ancestor.
     */
    List<MetaClass> getAncestors();

    /**
     * All descendants of the metaclass, recursively. Order is undefined.
     */
    Collection<MetaClass> getDescendants();

    /**
     * Get MetaProperty by its name.
     * @return MetaProperty instance, or null if no such property found
     */
    @Nullable
    MetaProperty getProperty(String name);

    /**
     * Get MetaProperty by its name.
     * @return MetaProperty instance. Throws exception if not found.
     */
    MetaProperty getPropertyNN(String name);

    /**
     * Returns MetaPropertyPath object, representing path to the property from the current class
     * @param propertyPath dot-separated string
     * @return MetaPropertyPath instance, or null if the input parameter doesn't represent a valid path.
     */
    @Nullable
    MetaPropertyPath getPropertyPath(String propertyPath);

    /**
     * @return collection of meta properties directly owned by this metaclass.
     */
    Collection<MetaProperty> getOwnProperties();

    /**
     * @return collection of meta properties owned by this metaclass and all its ancestors.
     */
    Collection<MetaProperty> getProperties();
}
