/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.web.filestorage;

import com.haulmont.cuba.gui.export.ClosedDataProviderException;
import com.vaadin.terminal.DownloadStream;
import com.haulmont.cuba.gui.export.ExportDataProvider;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author krivopustov
 * @version $Id$
 */
public class CloseableDownloadStream extends DownloadStream implements Closeable {

    private static final long serialVersionUID = -9108244903369511793L;

    private ExportDataProvider dataProvider;

    public CloseableDownloadStream(ExportDataProvider dataProvider, String contentType, String fileName)
            throws ClosedDataProviderException {
        super(dataProvider.provide(), contentType, fileName);
        this.dataProvider = dataProvider;
    }

    @Override
    public void close() throws IOException {
        dataProvider.close();
    }
}