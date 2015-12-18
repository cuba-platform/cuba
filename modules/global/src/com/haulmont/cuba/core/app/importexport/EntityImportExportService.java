/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app.importexport;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.View;

import java.util.Collection;

/**
 * Service is used for exporting a collection of entities and importing them.
 *
 * @author gorbunkov
 * @version $Id$
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
