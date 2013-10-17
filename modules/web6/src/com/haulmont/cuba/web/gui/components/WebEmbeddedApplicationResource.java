/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.export.ClosedDataProviderException;
import com.vaadin.terminal.ApplicationResource;
import com.vaadin.terminal.DownloadStream;
import com.vaadin.Application;
import com.vaadin.service.FileTypeResolver;
import com.haulmont.cuba.gui.export.ExportDataProvider;
import com.haulmont.cuba.web.filestorage.CloseableDownloadStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author krivopustov
 * @version $Id$
 */
public class WebEmbeddedApplicationResource implements ApplicationResource {

    private String filename;
    private String mimeType;
    private Application application;
    private ExportDataProvider dataProvider;

    private static final Log log = LogFactory.getLog(WebEmbeddedApplicationResource.class);

    public WebEmbeddedApplicationResource(ExportDataProvider dataProvider, String filename,
                                          Application application) {
        this.application = application;
        this.filename = filename;
        this.dataProvider = dataProvider;
        application.addResource(this);
    }

    public WebEmbeddedApplicationResource(ExportDataProvider dataProvider, String filename,
                                          String mimeType, Application application) {
        this.application = application;
        this.filename = filename;
        this.mimeType = mimeType;
        this.dataProvider = dataProvider;
        application.addResource(this);
    }

    @Override
    public DownloadStream getStream() {
        try {
            return new CloseableDownloadStream(dataProvider, getMIMEType(), filename);
        } catch (ClosedDataProviderException e) {
            log.error("Unable to open data provider", e);
            return null;
        }
    }

    @Override
    public Application getApplication() {
        return application;
    }

    @Override
    public String getFilename() {
        return filename;
    }

    @Override
    public long getCacheTime() {
        return 0;
    }

    @Override
    public int getBufferSize() {
        return 0;
    }

    @Override
    public String getMIMEType() {
        if (mimeType == null)
            return FileTypeResolver.getMIMEType(filename);
        else
            return mimeType;
    }
}