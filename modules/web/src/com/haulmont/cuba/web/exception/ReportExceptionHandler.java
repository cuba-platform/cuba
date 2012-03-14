/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.web.exception;

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.report.exception.FailedToConnectToOpenOfficeException;
import com.haulmont.cuba.report.exception.ReportingException;
import com.haulmont.cuba.report.exception.UnsupportedFormatException;
import com.haulmont.cuba.web.App;
import com.vaadin.ui.Window;

import javax.annotation.Nullable;

/**
 * Handles reporting exceptions.
 *
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class ReportExceptionHandler extends AbstractExceptionHandler {

    public ReportExceptionHandler() {
        super(
                ReportingException.class.getName(),
                FailedToConnectToOpenOfficeException.class.getName(),
                UnsupportedFormatException.class.getName()
        );
    }

    protected void doHandle(App app, String className, String message, @Nullable Throwable throwable) {
        String messageCode = "reportException.message";
        if (FailedToConnectToOpenOfficeException.class.getName().equals(className)) {
            messageCode = "reportException.failedConnectToOffice";
        } else if (UnsupportedFormatException.class.getName().equals(className)) {
            messageCode = "reportException.unsupportedFileFormat";
        }
        String msg = MessageProvider.getMessage(getClass(), messageCode);
        app.getAppWindow().showNotification(msg, Window.Notification.TYPE_ERROR_MESSAGE);
    }
}
