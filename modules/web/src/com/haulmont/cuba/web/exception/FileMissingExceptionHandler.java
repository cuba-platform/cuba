/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.exception;

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.export.FileMissingException;
import com.haulmont.cuba.web.App;
import com.vaadin.ui.Window;

import javax.annotation.Nullable;

/**
 * DEPRECATED! Will be removed in Release 3.2.
 *
 * Handles {@link FileMissingException}.
 *
 * <p>$Id$</p>
 *
 * @author artamonov
 */
@Deprecated
public class FileMissingExceptionHandler extends AbstractExceptionHandler {

    public FileMissingExceptionHandler() {
        super(FileMissingException.class.getName());
    }

    @Override
    protected void doHandle(App app, String className, String message, @Nullable Throwable throwable) {
        String fileName = throwable != null ? ((FileMissingException) throwable).getFileName() : "?";
        String msg = MessageProvider.formatMessage(getClass(), "fileNotFound.message", fileName);
        app.getAppWindow().showNotification(msg, Window.Notification.TYPE_WARNING_MESSAGE);
    }
}