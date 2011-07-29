/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.exception;

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.report.exception.FailedToConnectToOpenOfficeException;
import com.haulmont.cuba.report.exception.ReportFormatterException;
import com.haulmont.cuba.report.exception.UnsupportedFormatException;

/**
 * <p>$Id$</p>
 *
 * @author devyatkin
 */
public class ReportExceptionHandler extends AbstractExceptionHandler<ReportFormatterException> {

    public ReportExceptionHandler() {
        super(ReportFormatterException.class);
    }

    @Override
    protected void doHandle(Thread thread, ReportFormatterException e) {
        String messageCode = "reportException.message";
        if (e instanceof FailedToConnectToOpenOfficeException) {
            messageCode = "reportException.failedConnectToOffice";
        } else if (e instanceof UnsupportedFormatException) {
            messageCode = "reportException.unsupportedFileFormat";
        }
        String msg = MessageProvider.getMessage(getClass(), messageCode);
        App.getInstance().showNotificationPopup(msg, IFrame.NotificationType.ERROR);
    }
}
