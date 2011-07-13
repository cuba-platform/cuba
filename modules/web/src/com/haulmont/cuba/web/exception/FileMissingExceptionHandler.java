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
    protected void doHandle(FileMissingException t, App app) {
        String msg = MessageProvider.formatMessage(getClass(), "fileNotFoundWarning.message", t.getFileName());
        app.getAppWindow().showNotification(msg, Window.Notification.TYPE_WARNING_MESSAGE);
    }

    public boolean handle(FileMissingException e, App app) {
        doHandle(e, app);
        return true;
    }
}