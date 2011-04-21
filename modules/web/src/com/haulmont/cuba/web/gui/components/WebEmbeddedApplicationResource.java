/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 21.08.2009 16:12:14
 *
 * $Id$
 */
package com.haulmont.cuba.web.gui.components;

import com.vaadin.terminal.ApplicationResource;
import com.vaadin.terminal.DownloadStream;
import com.vaadin.Application;
import com.vaadin.service.FileTypeResolver;
import com.haulmont.cuba.gui.export.ExportDataProvider;
import com.haulmont.cuba.web.filestorage.CloseableDownloadStream;

public class WebEmbeddedApplicationResource implements ApplicationResource {

    private String filename;
    private Application application;
    private ExportDataProvider dataProvider;

    public WebEmbeddedApplicationResource(ExportDataProvider dataProvider, String filename, Application application)
    {
        this.application = application;
        this.filename = filename;
        this.dataProvider = dataProvider;
        application.addResource(this);
    }

    public DownloadStream getStream() {
        return new CloseableDownloadStream(dataProvider, getMIMEType(), filename);
    }

    public Application getApplication() {
        return application;
    }

    public String getFilename() {
        return filename;
    }

    public long getCacheTime() {
        return 0;
    }

    public int getBufferSize() {
        return 0;
    }

    public String getMIMEType() {
        return FileTypeResolver.getMIMEType(filename);
    }
}
