/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 21.08.2009 12:55:15
 *
 * $Id$
 */
package com.haulmont.cuba.web.rpt;

import com.itmill.toolkit.terminal.DownloadStream;
import com.haulmont.cuba.gui.export.ExportDataProvider;

import java.io.Closeable;
import java.io.IOException;

public class CloseableDownloadStream extends DownloadStream implements Closeable {

    private ExportDataProvider dataProvider;

    public CloseableDownloadStream(ExportDataProvider dataProvider, String contentType, String fileName) {
        super(dataProvider.provide(), contentType, fileName);
        this.dataProvider = dataProvider;
    }

    public void close() throws IOException {
        dataProvider.close();
    }
}
