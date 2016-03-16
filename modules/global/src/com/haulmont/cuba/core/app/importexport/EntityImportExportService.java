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

package com.haulmont.cuba.core.app.importexport;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.View;

import java.util.Collection;

/**
 * Service that is used for exporting a collection of entities and importing them.
 *
 */
public interface EntityImportExportService {

    String NAME = "cuba_EntityImportExportService";

    /**
     * <p>Serializes a collection of entities to JSON using {@link com.haulmont.cuba.core.app.serialization.EntitySerializationAPI EntitySerializationAPI}
     * and packs the JSON file into ZIP archive.</p>
     * <p>Serialization is described in the {@link com.haulmont.cuba.core.app.serialization.EntitySerializationAPI#toJson(Collection) EntitySerializationAPI#toJson(Collection)}
     * method documentation</p>
     *
     * @param entities a collection of entities to export
     * @return a byte array of zipped JSON file
     */
    byte[] exportEntities(Collection<? extends Entity> entities);

    /**
     * <p>Serializes a collection of entities to JSON using {@link com.haulmont.cuba.core.app.serialization.EntitySerializationAPI EntitySerializationAPI}
     * and packs the JSON file into ZIP archive. Before the serialization entities will be reloaded with the view passed as method parameter.</p>
     * <p>Serialization is described in the {@link com.haulmont.cuba.core.app.serialization.EntitySerializationAPI#toJson(Collection) EntitySerializationAPI#toJson(Collection)}
     * method documentation</p>
     *
     * @param entities a collection of entities to export
     * @param view before serialization to JSON entities will be reloaded with tis view
     * @return a byte array of zipped JSON file
     */
    byte[] exportEntities(Collection<? extends Entity> entities, View view);

    /**
     * Reads a zip archive that contains a JSON file, deserializes the JSON and persists deserialized entities
     * according to the rules, described by the {@code entityImportView} parameter. If the entity is not present in the database,
     * it will be saved. Otherwise the fields of the existing entity that are in the {@code entityImportView} will be updated.
     *
     * @param zipBytes byte array of ZIP archive with JSON file
     * @param entityImportView {@code EntityImportView} with the rules that describes how entities should be persisted.
     * @return a collection of entities that have been imported
     * @see EntityImportView
     */
    Collection<Entity> importEntities(byte[] zipBytes, EntityImportView entityImportView);

}
