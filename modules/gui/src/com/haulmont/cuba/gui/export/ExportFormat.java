/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 04.06.2009 12:32:50
 *
 * $Id$
 */
package com.haulmont.cuba.gui.export;

/**
 * Format of data exported by {@link ExportDisplay}
 */
public enum ExportFormat
{
    HTML("text/html", "html"),
    PDF("application/pdf", "pdf"),
    XLS("application/vnd.ms-excel", "xls");

    private String contentType;
    private String fileExt;

    private ExportFormat(String contentType, String fileExt) {
        this.contentType = contentType;
        this.fileExt = fileExt;
    }

    public String getContentType() {
        return contentType;
    }

    public String getFileExt() {
        return fileExt;
    }
}
