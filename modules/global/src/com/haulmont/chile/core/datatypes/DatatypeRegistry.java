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
import java.util.Set;

/**
 * Registry for {@link Datatype}s
 */
public interface DatatypeRegistry {

    String NAME = "cuba_DatatypeRegistry";

    /**
     * Get Datatype instance by its unique name
     * @return Datatype instance
     * @throws IllegalArgumentException if no datatype with the given name found
     */
    Datatype get(String name);

    /**
     * Get Datatype instance by the corresponding Java class. This method tries to find matching supertype too.
     * @return Datatype instance or null if not found
     */
    @Nullable
    <T> Datatype<T> get(Class<T> clazz);

    /**
     * Get Datatype instance by the corresponding Java class. This method tries to find matching supertype too.
     * @return Datatype instance
     * @throws IllegalArgumentException if no datatype suitable for the given type found
     */
    <T> Datatype<T> getNN(Class<T> clazz);

    /**
     * @return all registered Datatype names.
     */
    Set<String> getNames();

    /**
     * Register a datatype instance
     * @param datatype              datatype instance
     * @param defaultForJavaClass   true if the datatype should be default for a Java class handled by this datatype
     */
    void register(Datatype datatype, boolean defaultForJavaClass);
}