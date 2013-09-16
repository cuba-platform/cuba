/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.exception;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.NoSuchScreenException;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.web.App;

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
        String msg = AppBeans.get(Messages.class).getMessage(getClass(), "noSuchScreen.message");
        app.getWindowManager().showNotification(msg, IFrame.NotificationType.ERROR);
    }

    public void handle(NoSuchScreenException e, App app) {
        doHandle(app, NoSuchScreenException.class.getName(), e.getMessage(), e);
    }
}