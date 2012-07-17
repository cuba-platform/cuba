package com.haulmont.chile.core.model;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.AnnotatedElement;

/**
 * Metadata object reflecting an entity attribute.
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

        /** Reference type. Attribute of this type contains related entity,
         * and relation in this case is more strong then ASSOCIATION */
        AGGREGATION
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
