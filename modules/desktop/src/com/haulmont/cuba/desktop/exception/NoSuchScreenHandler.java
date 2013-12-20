/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.exception;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.gui.NoSuchScreenException;
import com.haulmont.cuba.gui.components.IFrame;

import javax.annotation.Nullable;

/**
 * Handles {@link NoSuchScreenException}.
 *
 * <p>$Id$</p>
 *
 * @author devyatkin
 */
public class NoSuchScreenHandler extends AbstractExceptionHandler {

    public NoSuchScreenHandler() {
        super(NoSuchScreenException.class.getName());
    }

    @Override
    protected void doHandle(Thread thread, String className, String message, @Nullable Throwable throwable) {
        String msg = AppBeans.get(Messages.class).getMessage(getClass(), "noSuchScreen.message");
        App.getInstance().getMainFrame().showNotification(msg, throwable != null ? throwable.getMessage() : null,
                IFrame.NotificationType.ERROR);
    }
}
