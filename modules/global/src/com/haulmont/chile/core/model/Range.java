package com.haulmont.chile.core.model;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Enumeration;

/**
 * Object encapsulating some common properties of MetaProperty
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
