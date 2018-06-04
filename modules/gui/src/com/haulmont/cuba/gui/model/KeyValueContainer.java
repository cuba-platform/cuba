/*
 * Copyright (c) 2008-2018 Haulmont.
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

package com.haulmont.cuba.gui.model;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.cuba.core.entity.KeyValueEntity;

import javax.annotation.Nullable;

public interface KeyValueContainer extends InstanceContainer<KeyValueEntity> {

    /**
     * Sets the name of a property that represents the entity id.
     * @return this instance for chaining
     */
    KeyValueContainer setIdName(String name);

    @Nullable
    String getIdName();

    /**
     * Adds a string property to the meta-class of this loader.
     * @return this instance for chaining
     */
    KeyValueContainer addProperty(String name);

    /**
     * Adds a property of the given Java class to the meta-class of this loader.
     * The Java class can be an entity or a datatype.
     * @return this instance for chaining
     */
    KeyValueContainer addProperty(String name, Class aClass);

    /**
     * Adds a property of the given datatype to the meta-class of this loader.
     * @return this instance for chaining
     */
    KeyValueContainer addProperty(String name, Datatype datatype);
}
