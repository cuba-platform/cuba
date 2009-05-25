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

import com.itmill.toolkit.terminal.DownloadStream;
import com.itmill.toolkit.ui.Window;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;

public class ReportDownloadWindow extends ReportOutputWindow
{
    private byte[] report;
    private String rptName;
    private ReportOutput output;

    public ReportDownloadWindow(byte[] report, String rptName, ReportOutput output) {
        super(rptName);
        this.report = report;
        this.rptName = rptName;
        this.output = output;
    }

    public DownloadStream handleURI(URL context, String relativeUri) {
        String contentType = output.getFormat().getContentType();
        String[] strings = rptName.split("[/\\\\]");
        String fileName = strings[strings.length-1] + "." + output.getFormat().getFileExt();
        InputStream is = new ByteArrayInputStream(report);
        DownloadStream downloadStream = new DownloadStream(is, contentType, fileName);
        if (output.isAttachment()) {
            downloadStream.setParameter("Content-Disposition", "attachment; filename=" + fileName);
        }
        return downloadStream;
    }
}
