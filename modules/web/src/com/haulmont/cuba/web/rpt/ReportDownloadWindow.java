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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;

public class ReportDownloadWindow extends ReportOutputWindow
{
    private byte[] data;
    private String name;
    private ReportOutput output;

    public ReportDownloadWindow(byte[] data, String name, ReportOutput output) {
        super(name);
        this.data = data;
        this.name = name;
        this.output = output;
    }

    public DownloadStream handleURI(URL context, String relativeUri) {
        String[] strings = name.split("[/\\\\]");
        String fileName = strings[strings.length-1];

        String contentType;
        if (output.getFormat() != null) {
            contentType = output.getFormat().getContentType();
            fileName = "." + output.getFormat().getFileExt();
        } else {
            contentType = "application/octet-stream";
        }

        try {
            fileName = URLEncoder.encode(fileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        InputStream is = new ByteArrayInputStream(data);
        DownloadStream downloadStream = new DownloadStream(is, contentType, fileName);
        if (output.isAttachment()) {
            downloadStream.setParameter("Content-Disposition", "attachment; filename=" + fileName);
        }
        return downloadStream;
    }
}
