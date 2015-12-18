/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app.importexport;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.View;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Collection;

/**
 * @author gorbunkov
 * @version $Id$
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
