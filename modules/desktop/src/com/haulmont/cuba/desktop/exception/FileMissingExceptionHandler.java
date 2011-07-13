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

/**
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class FileMissingExceptionHandler extends AbstractExceptionHandler<FileMissingException> {
    public FileMissingExceptionHandler() {
        super(FileMissingException.class);
    }

    @Override
    protected void doHandle(Thread thread, FileMissingException e) {
        String msg = MessageProvider.formatMessage(getClass(), "fileNotFoundWarning.message", e.getFileName());
        App.getInstance().showNotificationPopup(msg, IFrame.NotificationType.ERROR);
    }
}
