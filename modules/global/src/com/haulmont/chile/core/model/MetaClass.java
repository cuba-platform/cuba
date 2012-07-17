package com.haulmont.chile.core.model;

import java.util.Collection;

/**
 * Metadata object reflecting an entity class.
 */
public interface MetaClass extends MetadataObject<MetaClass> {

    /**
     * Containing MetaModel
     */
    MetaModel getModel();

    /**
     * Java class of the corresponding entity
     */
    Class getJavaClass();

    /**
     * Get MetaProperty by its name
     * @return MetaProperty instance, or null if no such property found
     */
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
     * Returns collection of meta properties directly owned by this class
     */
    Collection<MetaProperty> getOwnProperties();

    /**
     * Returns collection of meta properties owned by this class and all its ancestors
     */
    Collection<MetaProperty> getProperties();

    /**
     * Create an instance of entity reflected by this MetaClass
     */
    <T> T createInstance() throws InstantiationException, IllegalAccessException;
}
