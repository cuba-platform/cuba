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

import com.itmill.toolkit.terminal.ApplicationResource;
import com.itmill.toolkit.terminal.DownloadStream;
import com.itmill.toolkit.Application;
import com.itmill.toolkit.service.FileTypeResolver;
import com.haulmont.cuba.gui.export.ExportDataProvider;
import com.haulmont.cuba.web.rpt.CloseableDownloadStream;

public class EmbeddedApplicationResource implements ApplicationResource {

    private String filename;
    private Application application;
    private ExportDataProvider dataProvider;

    public EmbeddedApplicationResource(ExportDataProvider dataProvider, String filename, Application application)
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
