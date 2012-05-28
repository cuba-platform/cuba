/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.app.domain;

import com.haulmont.cuba.core.global.UserSessionProvider;
import com.haulmont.cuba.gui.export.ExportFormat;
import com.haulmont.cuba.gui.export.RestApiDataProvider;
import com.haulmont.cuba.web.filestorage.WebExportDisplay;

/**
 *
 * Class providing system data model
 *
 * @author korotkov
 * @version $Id$
 */
public class DomainProvider implements Runnable {
    @Override
    public void run() {
        WebExportDisplay exportDisplay = new WebExportDisplay(false, true);
        String query = "printDomain?s=" + UserSessionProvider.getUserSession().getId();
        RestApiDataProvider dataProvider = new RestApiDataProvider(query);
        exportDisplay.show(dataProvider, "Data model", ExportFormat.HTML);
    }
}
