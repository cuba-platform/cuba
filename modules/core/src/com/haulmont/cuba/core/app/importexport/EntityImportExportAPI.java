/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app.importexport;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.View;

import java.util.Collection;

/**
 * Class is used for exporting a collection of entities and importing them. See full javadocs in {@link EntityImportExportService}
 *
 * @author gorbunkov
 * @version $Id$
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
