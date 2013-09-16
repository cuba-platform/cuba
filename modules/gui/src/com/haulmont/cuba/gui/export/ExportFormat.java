/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.export;

/**
 * Format of data exported by {@link ExportDisplay}.
 *
 * @author krivopustov
 * @version $Id$
 */
public enum ExportFormat {

    HTML("text/html", "html"),
    HTM("text/html", "htm"),

    PDF("application/pdf", "pdf"),
    XLS("application/vnd.ms-excel", "xls"),
    XLSX("application/vnd.ms-excel", "xlsx"),
    RTF("application/rtf", "rtf"),
    DOC("application/doc", "doc"),
    DOCX("application/docx", "docx"),
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
