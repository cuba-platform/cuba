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

package com.haulmont.cuba.core.app.serialization;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.View;

import java.util.Collection;

/**
 * Class that is used for serialization and deserialization of entities to JSON.
 *
 */
public interface EntitySerializationAPI {

    String NAME = "cuba_EntitySerialization";

    /**
     * Serialize a single entity to the JSON format. Method works like the {@link #toJson(Collection)}
     * method with a collection containing a single entity as an argument.
     *
     * @param entity an entity to serialize
     * @return a string that represent a JSON array which contains a single entity object.
     */
    String toJson(Entity entity);

    /**
     * <p>Serialize a collection of entities to JSON format. The method will return a string
     * that represents a JSON array of entity objects.</p>
     * <p>Each object contains all fields of entity graph that are not null. Entity id value is written in the
     * format that is used by {@link com.haulmont.cuba.core.global.EntityLoadInfo} class, e.g:
     * {@code sec$User-60885987-1b61-4247-94c7-dff348347f93}</p>
     *
     * @param entities a collection of entities to serialize
     * @return a string that represent a JSON array which contains entity objects.
     */
    String toJson(Collection<? extends Entity> entities);

    /**
     * Serialize a single entity to the JSON format. Method works like the {@link #toJson(Collection, View)}
     * method with a collection containing a single entity as an argument.
     *
     * @param entity an entity to serialize
     * @param view a view that restricts an entity instance graph during the serialization
     * @return a string that represent a JSON array which contains a single entity object.
     */
    String toJson(Entity entity, View view);

    /**
     * <p>Serialize a collection of entities to JSON format. The method will return a string
     * that represents a JSON array of entity objects.</p>
     * <p>Each object contains all fields of entity graph that are not null are included to the {@code view} parameter.
     * Entity id value is written in the format that is used by {@link com.haulmont.cuba.core.global.EntityLoadInfo}
     * class, e.g: {@code sec$User-60885987-1b61-4247-94c7-dff348347f93}.</p>
     *
     * @param entities a collection of entities to serialize
     * @param view a view that restricts an entity instance graph during the serialization
     * @return a string that represent a JSON array which contains entity objects.
     */
    String toJson(Collection<? extends Entity> entities, View view);

    /**
     * Deserialize a JSON string to the collection of entities.
     *
     * @param json a string that represents a JSON array of entity objects
     * @param <T> an entity class
     * @return a collection of entities
     */
    <T extends Entity> Collection<T> fromJson(String json);
}
