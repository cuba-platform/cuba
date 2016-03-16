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

package com.haulmont.cuba.core.global;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.annotation.ExtendedBy;
import com.haulmont.cuba.core.entity.annotation.Extends;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * Encapsulates functionality for working with extended entities.
 *
 */
@Component(ExtendedEntities.NAME)
public class ExtendedEntities {

    public static final String NAME = "cuba_ExtendedEntities";

    @Inject
    protected Metadata metadata;

    protected Map<Class, MetaClass> replacedMetaClasses = new HashMap<>();

    /**
     * Default constructor used by container at runtime and in server-side integration tests.
     */
    public ExtendedEntities() {
    }

    /**
     * Constructor used in client-side tests.
     */
    public ExtendedEntities(Metadata metadata) {
        this.metadata = metadata;
    }

    /**
     * Searches for an extended entity and returns it if found, otherwise returns the original entity.
     *
     * @param originalMetaClass original entity
     * @return extended or original entity
     */
    public Class getEffectiveClass(MetaClass originalMetaClass) {
        Class extClass = getExtendedClass(originalMetaClass);
        return extClass == null ? originalMetaClass.getJavaClass() : extClass;
    }

    /**
     * Searches for an extended entity and returns it if found, otherwise returns the original entity.
     *
     * @param originalClass original entity
     * @return extended or original entity
     */
    public Class getEffectiveClass(Class originalClass) {
        return getEffectiveClass(metadata.getSession().getClassNN(originalClass));
    }

    /**
     * Searches for an extended entity and returns it if found, otherwise returns the original entity.
     *
     * @param entityName original entity
     * @return extended or original entity
     */
    public Class getEffectiveClass(String entityName) {
        return getEffectiveClass(metadata.getSession().getClassNN(entityName));
    }

    /**
     * Searches for an extended entity and returns it if found, otherwise returns the original entity.
     *
     * @param originalMetaClass original entity
     * @return extended or original entity
     */
    public MetaClass getEffectiveMetaClass(MetaClass originalMetaClass) {
        return metadata.getSession().getClassNN(getEffectiveClass(originalMetaClass));
    }

    /**
     * Searches for an extended entity and returns it if found, otherwise returns the original entity.
     *
     * @param originalClass original entity
     * @return extended or original entity
     */
    public MetaClass getEffectiveMetaClass(Class originalClass) {
        return metadata.getSession().getClassNN(getEffectiveClass(originalClass));
    }

    /**
     * Searches for an extended entity and returns it if found, otherwise returns the original entity.
     *
     * @param entityName original entity
     * @return extended or original entity
     */
    public MetaClass getEffectiveMetaClass(String entityName) {
        return getEffectiveMetaClass(metadata.getSession().getClassNN(entityName));
    }

    /**
     * Searches for an extended entity and returns it if found.
     *
     * @param originalMetaClass original entity
     * @return extended entity or null if the provided entity has no extension
     */
    @Nullable
    public Class getExtendedClass(MetaClass originalMetaClass) {
        return (Class) originalMetaClass.getAnnotations().get(ExtendedBy.class.getName());
    }

    /**
     * Searches for an original entity for the provided extended entity.
     *
     * @param extendedMetaClass extended entity
     * @return original entity or null if the provided entity is not an extension
     */
    @Nullable
    public Class getOriginalClass(MetaClass extendedMetaClass) {
        return (Class) extendedMetaClass.getAnnotations().get(Extends.class.getName());
    }

    /**
     * Searches for an original entity for the provided extended entity.
     *
     * @param extendedMetaClass extended entity
     * @return original entity or null if the provided entity is not an extension
     */
    @Nullable
    public MetaClass getOriginalMetaClass(MetaClass extendedMetaClass) {
        Class originalClass = getOriginalClass(extendedMetaClass);
        if (originalClass == null) {
            return null;
        }

        MetaClass metaClass = replacedMetaClasses.get(originalClass);
        if (metaClass != null) {
            return metaClass;
        }

        return metadata.getSession().getClassNN(originalClass);
    }

    /**
     * Searches for an original entity for the provided extended entity.
     *
     * @param extendedEntityName extended entity
     * @return original entity or null if the provided entity is not an extension
     */
    @Nullable
    public MetaClass getOriginalMetaClass(String extendedEntityName) {
        return getOriginalMetaClass(metadata.getSession().getClassNN(extendedEntityName));
    }

    /**
     * @return original meta class or received meta class if it's not extended
     */
    public MetaClass getOriginalOrThisMetaClass(MetaClass metaClass) {
        MetaClass originalMetaClass = getOriginalMetaClass(metaClass);
        return originalMetaClass != null ? originalMetaClass : metaClass;
    }

    /**
     * INTERNAL. Import replaced meta class from metadata.
     */
    public void registerReplacedMetaClass(MetaClass metaClass) {
        replacedMetaClasses.put(metaClass.getJavaClass(), metaClass);
    }
}