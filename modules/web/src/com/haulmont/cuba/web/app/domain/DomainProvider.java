/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.app.domain;

import com.haulmont.cuba.core.global.UserSessionProvider;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.export.ExportDataProvider;
import com.haulmont.cuba.gui.export.ExportFormat;
import com.haulmont.cuba.gui.export.ResourceException;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.App;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * Simple class providing system data model using REST Api
 *
 * @author korotkov
 * @version $Id$
 */
public class DomainProvider implements Runnable {
    @Override
    public void run() {
        UserSession session = UserSessionProvider.getUserSession();
        URL url = App.getInstance().getURL();
        try {
            url = new URL(url, "../app-core/api/printDomain?s=" + session.getId());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        try {
            final InputStream stream = (InputStream) url.getContent();
            AppConfig.createExportDisplay().show(new ExportDataProvider() {
                @Override
                public InputStream provide() throws ResourceException {
                    return stream;
                }

                @Override
                public void close() {
                    try {
                        stream.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }, "DomainProvider", ExportFormat.HTML);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
