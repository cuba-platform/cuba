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

/**
 * Metadata model entry point.
 * <p/>Metadata consists of a set of interrelated {@link MetaClass} instances.
 * <p/>The whole metadata is split into named models. One can obtain {@link MetaClass} instances through the
 * {@link MetaModel} references or directly from the Session.
 *
 */
public interface Session {

    MetaModel getModel(String name);

    Collection<MetaModel> getModels();

    /**
     * Search MetaClass by its name in the whole metamodel.
     * @param name  entity name
     * @return      MetaClass instance or null if not found
     */
    @Nullable
    MetaClass getClass(String name);

    /**
     * Search MetaClass by its name in the whole metamodel.
     * @param name  entity name
     * @return      MetaClass instance. Throws exception if not found.
     */
    MetaClass getClassNN(String name);

    /**
     * Search MetaClass by the corresponding Java class in the whole metamodel.
     * @param clazz Java class defining the entity
     * @return      MetaClass instance or null if not found
     */
    @Nullable
    MetaClass getClass(Class<?> clazz);

    /**
     * Search MetaClass by the corresponding Java class in the whole metamodel.
     * @param clazz Java class defining the entity
     * @return      MetaClass instance. Throws exception if not found.
     */
    MetaClass getClassNN(Class<?> clazz);

    /**
     * @return collection of all MetaClasses in the whole metamodel
     */
    Collection<MetaClass> getClasses();
}
