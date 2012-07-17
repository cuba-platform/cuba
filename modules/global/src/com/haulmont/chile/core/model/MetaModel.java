package com.haulmont.chile.core.model;

import java.util.Collection;

/**
 * Container and entry point for metadata objects
 */
public interface MetaModel extends MetadataObject<MetaModel> {

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
