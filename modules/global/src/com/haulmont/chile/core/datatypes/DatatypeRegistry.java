/*
 * Copyright (c) 2008-2017 Haulmont.
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
 */

package com.haulmont.chile.core.datatypes;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Set;

/**
 * Registry for {@link Datatype}s
 */
public interface DatatypeRegistry {

    String NAME = "cuba_DatatypeRegistry";

    /**
     * Get Datatype instance by its unique id
     * @return Datatype instance
     * @throws IllegalArgumentException if no datatype with the given name found
     */
    Datatype get(String id);

    /**
     * Get Datatype instance by the corresponding Java class. This method tries to find matching supertype too.
     * @return Datatype instance or null if not found
     */
    @Nullable
    <T> Datatype<T> get(Class<T> javaClass);

    /**
     * Get Datatype instance by the corresponding Java class. This method tries to find matching supertype too.
     * @return Datatype instance
     * @throws IllegalArgumentException if no datatype suitable for the given type found
     */
    <T> Datatype<T> getNN(Class<T> javaClass);

    /**
     * Returns an ID of the given datatype in the registry.
     * @throws IllegalArgumentException if the datatype is not registered
     */
    String getId(Datatype datatype);

    /**
     * @param datatype {@link Datatype} instance
     * @return {@link Optional} of the given {@code datatype} id
     */
    Optional<String> getOptionalId(Datatype datatype);

    /**
     * Returns an ID of a first datatype handling the given Java class.
     * @throws IllegalArgumentException if no datatypes handle the given Java class
     */
    String getIdByJavaClass(Class<?> javaClass);

    /**
     * @return all registered datatype identifiers.
     */
    Set<String> getIds();

    /**
     * Register a datatype instance
     * @param datatype              datatype instance
     * @param id                    unique registration id
     * @param defaultForJavaClass   true if the datatype should be default for a Java class handled by this datatype
     */
    void register(Datatype datatype, String id, boolean defaultForJavaClass);
}