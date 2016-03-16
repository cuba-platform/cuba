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

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Enumeration;

/**
 * Object encapsulating common properties of {@link MetaProperty}.
 *
 */
public interface Range {

    /** Is this property of a simple type? */
    boolean isDatatype();

    /** Is this property a reference? */
    boolean isClass();

    /** Is this property an enumeration? */
    boolean isEnum();

    /** Get this property Datatype. Throws IllegalStateException if this property is not of a simple type. */
    <T> Datatype<T> asDatatype();

    /** Get this property Enumeration. Throws IllegalStateException if this property is not an enumeration. */
    Enumeration asEnumeration();

    /** Get this property MetaClass. Throws IllegalStateException if this property is not a reference. */
    MetaClass asClass();

    /** Is this property ordered? True for ordered collections. */
    boolean isOrdered();

    /** Relation type for reference property */
    enum Cardinality {
        NONE,
        ONE_TO_ONE,
        MANY_TO_ONE,
        ONE_TO_MANY,
        MANY_TO_MANY;

        public boolean isMany() {
            return equals(ONE_TO_MANY) || equals(MANY_TO_MANY);
        }
    }

    /** Get relation type for this property if it is a reference.  */
    Cardinality getCardinality();
}
