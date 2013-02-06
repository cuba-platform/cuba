/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 28.07.2009 10:08:15
 *
 * $Id$
 */
package com.haulmont.cuba.web.exception;

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.NoSuchScreenException;
import com.haulmont.cuba.web.App;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

import javax.annotation.Nullable;

/**
 * Handles {@link NoSuchScreenException}.
 *
 * @author krivopustov
 * @version $Id$
 */
public class NoSuchScreenHandler extends AbstractExceptionHandler {

    public NoSuchScreenHandler() {
        super(NoSuchScreenException.class.getName());
    }

    @Override
    protected void doHandle(App app, String className, String message, @Nullable Throwable throwable) {
        String msg = MessageProvider.getMessage(getClass(), "noSuchScreen.message");
        app.getAppUI().showNotification(msg, Notification.TYPE_ERROR_MESSAGE);
    }

    public void handle(NoSuchScreenException e, App app) {
        doHandle(app, NoSuchScreenException.class.getName(), e.getMessage(), e);
    }
}