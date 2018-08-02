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
 * Class that is used for exporting a collection of entities and importing them. See full java docs in the {@link
 * EntityImportExportService}
 */
public interface EntityImportExportAPI {

    String NAME = "cuba_EntityImportExport";

    /**
     * See documentation for {@link EntityImportExportService#exportEntitiesToZIP(Collection)}
     */
    byte[] exportEntitiesToZIP(Collection<? extends Entity> entities);

    /**
     * See documentation for {@link EntityImportExportService#exportEntitiesToZIP(Collection, View)}
     */
    byte[] exportEntitiesToZIP(Collection<? extends Entity> entities, View view);

    /**
     * See documentation for {@link EntityImportExportService#exportEntitiesToJSON(Collection, View)}
     */
    String exportEntitiesToJSON(Collection<? extends Entity> entities, View view);

    /**
     * See documentation for {@link EntityImportExportService#exportEntitiesToJSON(Collection)}
     */
    String exportEntitiesToJSON(Collection<? extends Entity> entities);

    /**
     * See documentation for {@link EntityImportExportService#importEntitiesFromJSON(String, EntityImportView)}
     */
    Collection<Entity> importEntitiesFromJson(String json, EntityImportView view);

    /**
     * See documentation for {@link EntityImportExportService#importEntitiesFromZIP(byte[], EntityImportView)}
     */
    Collection<Entity> importEntitiesFromZIP(byte[] zipBytes, EntityImportView entityImportView);

    /**
     * See documentation for {@link EntityImportExportService#importEntities(Collection, EntityImportView)}
     */
    Collection<Entity> importEntities(Collection<? extends Entity> entities, EntityImportView view);

    /**
     * See documentation for {@link EntityImportExportService#importEntities(Collection, EntityImportView, boolean)}
     */
    Collection<Entity> importEntities(Collection<? extends Entity> entities, EntityImportView importView, boolean validate);
}
