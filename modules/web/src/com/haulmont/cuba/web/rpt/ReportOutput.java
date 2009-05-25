/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 25.05.2009 11:33:25
 *
 * $Id$
 */
package com.haulmont.cuba.web.rpt;

public class ReportOutput {

    public enum Format
    {
        HTML("text/html", "html"),
        PDF("application/pdf", "pdf"),
        XLS("application/xls", "xls");

        private String contentType;
        private String fileExt;

        Format(String contentType, String fileExt) {
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

    private Format format;
    private boolean newWindow;
    private boolean attachment;

    public ReportOutput(Format format) {
        this.format = format;
    }

    public boolean isAttachment() {
        return attachment;
    }

    public ReportOutput setAttachment(boolean attachment) {
        this.attachment = attachment;
        return this;
    }

    public Format getFormat() {
        return format;
    }

    public ReportOutput setFormat(Format format) {
        this.format = format;
        return this;
    }

    public boolean isNewWindow() {
        return newWindow;
    }

    public ReportOutput setNewWindow(boolean newWindow) {
        this.newWindow = newWindow;
        return this;
    }
}

