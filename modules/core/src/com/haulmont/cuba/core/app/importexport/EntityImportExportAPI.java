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
 * Class that is used for exporting a collection of entities and importing them. See full javadocs in {@link EntityImportExportService}
 *
 */
public interface EntityImportExportAPI {

    String NAME = "cuba_EntityImportExport";

    /**
     * See documentation for {@link EntityImportExportService#exportEntities(Collection)}
     */
    byte[] exportEntities(Collection<? extends Entity> entities);

    /**
     * See documentation for {@link EntityImportExportService#exportEntities(Collection, View)}
     */
    byte[] exportEntities(Collection<? extends Entity> entities, View view);

    /**
     * See documentation for {@link EntityImportExportService#importEntities(byte[], EntityImportView)}
     */
    Collection<Entity> importEntities(byte[] zipBytes, EntityImportView entityImportView);
}
