/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Yuryi Artamonov
 * Created: 06.10.2010 14:57:32
 *
 * $Id$
 */
package com.haulmont.cuba.web.exception;

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.report.exception.FailedToConnectToOpenOfficeException;
import com.haulmont.cuba.report.exception.ReportFormatterException;
import com.haulmont.cuba.report.exception.UnsupportedFormatException;
import com.haulmont.cuba.web.App;
import com.vaadin.ui.Window;

public class ReportExceptionHandler extends AbstractExceptionHandler<ReportFormatterException> {

    public ReportExceptionHandler() {
        super(ReportFormatterException.class);
    }

    protected void doHandle(ReportFormatterException t, App app) {
        String messageCode = "reportException.message";
        if (t instanceof FailedToConnectToOpenOfficeException) {
            messageCode = "reportException.failedConnectToOffice";
        } else if (t instanceof UnsupportedFormatException) {
            messageCode = "reportException.unsupportedFileFormat";
        }
        String msg = MessageProvider.getMessage(getClass(), messageCode);
        app.getAppWindow().showNotification(msg, Window.Notification.TYPE_ERROR_MESSAGE);
    }

    public boolean handle(ReportFormatterException e, App app) {
        doHandle(e, app);
        return true;
    }
}
