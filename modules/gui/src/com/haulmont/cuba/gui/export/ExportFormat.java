/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.export;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Format of data exported by {@link ExportDisplay}.
 *
 * @author krivopustov
 * @version $Id$
 */
public final class ExportFormat implements Serializable {
    private static final long serialVersionUID = -8448531804422711852L;

    public static final ExportFormat HTML = new ExportFormat("text/html", "html");
    public static final ExportFormat HTM = new ExportFormat("text/html", "htm");
    public static final ExportFormat PDF = new ExportFormat("application/pdf", "pdf");
    public static final ExportFormat XLS = new ExportFormat("application/vnd.ms-excel", "xls");
    public static final ExportFormat XLSX = new ExportFormat("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "xlsx");
    public static final ExportFormat RTF = new ExportFormat("application/rtf", "rtf");
    public static final ExportFormat DOC = new ExportFormat("application/doc", "doc");
    public static final ExportFormat DOCX = new ExportFormat("application/vnd.openxmlformats-officedocument.wordprocessingml.document", "docx");
    public static final ExportFormat XML = new ExportFormat("text/xml", "xml");
    public static final ExportFormat CSV = new ExportFormat("application/csv", "csv");
    public static final ExportFormat JPEG = new ExportFormat("image/jpeg", "jpeg");
    public static final ExportFormat JPG = new ExportFormat("image/jpeg", "jpg");
    public static final ExportFormat PNG = new ExportFormat("image/png", "png");
    public static final ExportFormat RAR = new ExportFormat("application/x-rar-compressed", "rar");
    public static final ExportFormat ZIP = new ExportFormat("application/zip", "zip");
    public static final ExportFormat GZ = new ExportFormat(" application/x-gzip", "gz");
    public static final ExportFormat OCTET_STREAM = new ExportFormat("application/octet-stream", "");

    public static final List<ExportFormat> DEFAULT_FORMATS = Collections.unmodifiableList(
            Arrays.asList(HTML, HTM, PDF, XLS, XLSX, RTF, DOC, DOCX, XML, CSV, JPEG, JPG, PNG, RAR, GZ, ZIP, OCTET_STREAM));

    protected final String contentType;
    protected final String fileExt;

    public ExportFormat(String contentType, String fileExt) {
        this.contentType = contentType;
        this.fileExt = fileExt;
    }

    public static ExportFormat getByExtension(String extension) {
        if (StringUtils.isEmpty(extension)) {
            return OCTET_STREAM;
        }

        String extLowerCase = StringUtils.lowerCase(extension);

        List<ExportFormat> formats = DEFAULT_FORMATS;
        for (ExportFormat f : formats) {
            if (f.getFileExt().equals(extLowerCase))
                return f;
        }
        return OCTET_STREAM;
    }

    public String getContentType() {
        return contentType;
    }

    public String getFileExt() {
        return fileExt;
    }
}