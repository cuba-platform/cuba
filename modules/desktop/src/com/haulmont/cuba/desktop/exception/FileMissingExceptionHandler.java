/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.exception;

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.export.FileMissingException;

import javax.annotation.Nullable;

/**
 * Handles {@link FileMissingException}.
 *
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class FileMissingExceptionHandler extends AbstractExceptionHandler {

    public FileMissingExceptionHandler() {
        super(FileMissingException.class.getName());
    }

    @Override
    protected void doHandle(Thread thread, String className, String message, @Nullable Throwable throwable) {
        String fileName = throwable != null ? ((FileMissingException) throwable).getFileName() : "?";
        String msg = MessageProvider.formatMessage(getClass(), "fileNotFoundWarning.message", fileName);
        App.getInstance().showNotificationPopup(msg, IFrame.NotificationType.ERROR);
    }
}
