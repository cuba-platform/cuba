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

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.View;

import javax.annotation.Nullable;
import java.util.Collection;

/**
 * Class that is used for serialization and deserialization of entities to JSON.
 */
public interface EntitySerializationAPI {

    String NAME = "cuba_EntitySerialization";

    /**
     * Serializes a single entity to the JSON object graph.
     * <p>
     * If the {@code view} parameter is null then all loaded entity properties will be presented in JSON, otherwise only
     * loaded properties that are in the view will be in the JSON object.
     * <p>
     * The {@code options} parameter specify come additional options for the serialization process. For example, a
     * repeated entities may be replaced with the object with the only "id" property, making the result JSON more
     * compact. See {@link EntitySerializationOption} for details.
     * <p>
     * Additionally, an "_entityName" property is added to the JSON objects that represent an entity.
     *
     * @param entity  an entity to be serialized
     * @param view    a view that defines which entity properties should be added to the result JSON object
     * @param options options specifying how an entity should be serialized
     * @return a string that represents a JSON object
     */
    String toJson(Entity entity,
                  @Nullable View view,
                  EntitySerializationOption... options);

    /**
     * Serialized a collection of entities to the JSON array. Method works like the {@link #toJson(Entity, View,
     * EntitySerializationOption...)}, but return a JSON array as a result.
     *
     * @param entities a list of entities to be serialized
     * @param view     a view that defines which entity properties should be added to the result JSON object
     * @param options  options specifying how an entity should be serialized
     * @return a string that represents a JSON array of objects.
     */
    String toJson(Collection<? extends Entity> entities,
                  @Nullable View view,
                  EntitySerializationOption... options);

    /**
     * An overloaded version of the {@link #toJson(Entity, View, EntitySerializationOption...)} method with a null
     * {@code view} parameter and with no serialization options.
     *
     * @param entity an entity to be serialized
     * @return a string that represents a JSON object
     */
    String toJson(Entity entity);

    /**
     * An overloaded version of the {@link #toJson(Collection, View, EntitySerializationOption...)} method with a null
     * {@code view} parameter and with no serialization options.
     *
     * @param entities a collection of entities to be serialized
     * @return a string that represent a JSON array which contains entity objects
     */
    String toJson(Collection<? extends Entity> entities);


    /**
     * Deserializes a JSON object to the entity.
     * <p>
     * The {@code metaClass} parameter defines a result entity metaClass. It is optional. It must be defined if the JSON
     * object doesn't contain an "_entityName" property.
     * <p>
     * An entity may be serialized to the JSON in slightly different formats. The format is defined by the {@code
     * options} parameter. See {@link EntitySerializationOption} for details.
     *
     * @param json      a string that represents a JSON object
     * @param metaClass a metaClass of the entity that will be created
     * @param options   options specifying how a JSON object graph was serialized
     * @return an entity
     */
    <T extends Entity> T entityFromJson(String json,
                                        @Nullable MetaClass metaClass,
                                        EntitySerializationOption... options);

    /**
     * Deserializes a JSON array of objects to entities collection
     *
     * @param json      a string that represents a JSON array of objects
     * @param metaClass a metaClass of the entities that will be created
     * @param options   options specifying how a JSON object graph was serialized
     * @return an entities collection
     */
    <T extends Entity> Collection<T> entitiesCollectionFromJson(String json,
                                                                @Nullable MetaClass metaClass,
                                                                EntitySerializationOption... options);
}
