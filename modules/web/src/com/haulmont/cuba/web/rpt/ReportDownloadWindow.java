/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 21.05.2009 18:11:15
 *
 * $Id$
 */
package com.haulmont.cuba.web.rpt;

import com.haulmont.cuba.web.filestorage.CloseableDownloadStream;
import com.vaadin.terminal.DownloadStream;
import com.haulmont.cuba.gui.export.ExportDataProvider;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * @deprecated Please use {@link com.haulmont.cuba.web.filestorage.ResourceWindow}
 */
@Deprecated
public class ReportDownloadWindow extends ReportOutputWindow
{
    private String name;
    private ReportOutput output;
    private ExportDataProvider dataProvider;

    public ReportDownloadWindow(ExportDataProvider dataProvider, String name, ReportOutput output) {
        super(name);
        this.name = name;
        this.output = output;
        this.dataProvider = dataProvider;
    }

    public DownloadStream handleURI(URL context, String relativeUri) {
        String[] strings = name.split("[/\\\\]");
        String fileName = strings[strings.length-1];

        String contentType;
        if (output.getFormat() != null) {
            contentType = output.getFormat().getContentType();
            fileName += "." + output.getFormat().getFileExt();
        } else {
            contentType = "application/octet-stream";
        }

        try {
            fileName = URLEncoder.encode(fileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        DownloadStream downloadStream = new CloseableDownloadStream(dataProvider, contentType, fileName);
        if (output.isAttachment()) {
            downloadStream.setParameter("Content-Disposition", "attachment; filename=" + fileName);
        }
        return downloadStream;
    }

    @Override
    public void dispose() {
        dataProvider.close();
        super.dispose();
    }
}
