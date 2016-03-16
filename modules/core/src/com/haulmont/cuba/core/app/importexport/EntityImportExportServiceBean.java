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

/**
 */
@Service(EntityImportExportService.NAME)
public class EntityImportExportServiceBean implements EntityImportExportService {

    @Inject
    protected EntityImportExportAPI entityImportExport;

    @Override
    public byte[] exportEntities(Collection<? extends Entity> entities) {
        return entityImportExport.exportEntities(entities);
    }

    @Override
    public byte[] exportEntities(Collection<? extends Entity> entities, View view) {
        return entityImportExport.exportEntities(entities, view);
    }

    @Override
    public Collection<Entity> importEntities(byte[] zipBytes, EntityImportView view) {
        return entityImportExport.importEntities(zipBytes, view);
    }
}
