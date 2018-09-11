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
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Collection;

@Service(EntityImportExportService.NAME)
public class EntityImportExportServiceBean implements EntityImportExportService {

    @Inject
    protected EntityImportExportAPI entityImportExport;

    @Override
    public byte[] exportEntitiesToZIP(Collection<? extends Entity> entities) {
        return entityImportExport.exportEntitiesToZIP(entities);
    }

    @Override
    public byte[] exportEntitiesToZIP(Collection<? extends Entity> entities, View view) {
        return entityImportExport.exportEntitiesToZIP(entities, view);
    }

    @Override
    public String exportEntitiesToJSON(Collection<? extends Entity> entities) {
        return entityImportExport.exportEntitiesToJSON(entities);
    }

    @Override
    public String exportEntitiesToJSON(Collection<? extends Entity> entities, View view) {
        return entityImportExport.exportEntitiesToJSON(entities, view);
    }

    @Override
    public Collection<Entity> importEntitiesFromJSON(String json, EntityImportView entityImportView) {
        return entityImportExport.importEntitiesFromJson(json, entityImportView);
    }

    @Override
    public Collection<Entity> importEntitiesFromZIP(byte[] zipBytes, EntityImportView view) {
        return entityImportExport.importEntitiesFromZIP(zipBytes, view);
    }

    @Override
    public Collection<Entity> importEntities(Collection<? extends Entity> entities, EntityImportView entityImportView) {
        return entityImportExport.importEntities(entities, entityImportView);
    }

    @Override
    public Collection<Entity> importEntities(Collection<? extends Entity> entities, EntityImportView importView, boolean validate) {
        return entityImportExport.importEntities(entities, importView, validate);
    }

    @Override
    public Collection<Entity> importEntities(Collection<? extends Entity> entities, EntityImportView importView, boolean validate, boolean optimisticLocking) {
        return entityImportExport.importEntities(entities, importView, validate, optimisticLocking);
    }
}