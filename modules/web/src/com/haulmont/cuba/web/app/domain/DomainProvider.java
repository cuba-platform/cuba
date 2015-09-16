/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.app.domain;

import com.haulmont.cuba.core.app.DomainDescriptionService;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.export.ExportFormat;
import com.haulmont.cuba.web.filestorage.WebExportDisplay;

import java.nio.charset.StandardCharsets;

/**
 * Class providing domain model description. It can be called from the main menu.
 *
 * @author korotkov
 * @version $Id$
 */
public class DomainProvider implements Runnable {

    @Override
    public void run() {
        DomainDescriptionService service = AppBeans.get(DomainDescriptionService.NAME);
        String description = service.getDomainDescription();

        WebExportDisplay exportDisplay = new WebExportDisplay(true);
        exportDisplay.show(description.getBytes(StandardCharsets.UTF_8), "DomainDescription", ExportFormat.HTML);
    }
}