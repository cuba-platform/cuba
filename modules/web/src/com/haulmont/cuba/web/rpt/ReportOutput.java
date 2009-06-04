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

import com.haulmont.cuba.gui.export.ExportFormat;

public class ReportOutput {

    private ExportFormat format;
    private boolean newWindow;
    private boolean attachment;

    public ReportOutput(ExportFormat format) {
        this.format = format;
    }

    public boolean isAttachment() {
        return attachment;
    }

    public ReportOutput setAttachment(boolean attachment) {
        this.attachment = attachment;
        return this;
    }

    public ExportFormat getFormat() {
        return format;
    }

    public ReportOutput setFormat(ExportFormat format) {
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

