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
public enum ExportFormat {
    HTML("text/html", "html"),
    HTM("text/html", "htm"),

    PDF("application/pdf", "pdf"),
    XLS("application/vnd.ms-excel", "xls"),
    RTF("application/rtf", "rtf"),
    DOC("application/doc", "doc"),
    XML("text/xml", "xml"),
    CSV("application/csv", "csv"),

    JPEG("image/jpeg", "jpeg"),
    JPG("image/jpeg", "jpg"),
    PNG("image/png", "png"),

    RAR("application/x-rar-compressed", "rar"),
    ZIP("application/zip", "zip"),
    OCTET_STREAM("application/octet-stream", "");

    private String contentType;
    private String fileExt;

    private ExportFormat(String contentType, String fileExt) {
        this.contentType = contentType;
        this.fileExt = fileExt;
    }

    public static ExportFormat getByExtension(String extension) {
        ExportFormat format = null;
        ExportFormat[] formats = values();
        int i = 0;
        while ((i < formats.length) && (format == null)) {
            if (formats[i].fileExt.equalsIgnoreCase(extension))
                format = formats[i];
            i++;
        }
        return format;
    }

    public String getContentType() {
        return contentType;
    }

    public String getFileExt() {
        return fileExt;
    }
}
