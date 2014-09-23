/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.exception;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.RemoteException;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.web.App;
import com.vaadin.server.ErrorEvent;
import org.apache.commons.lang.exception.ExceptionUtils;

import javax.annotation.Nullable;
import java.sql.SQLException;
import java.util.List;

/**
 * @author novikov
 * @version $Id$
 */
public class MssqlDateOutOfRangeExceptionHandler implements ExceptionHandler {

    private String className;

    private static final String MESSAGE = "Only dates between January 1, 1753 and December 31, 9999 are accepted";

    public MssqlDateOutOfRangeExceptionHandler() {
        this.className = SQLException.class.getName();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean handle(ErrorEvent event, App app) {
        Throwable exception = event.getThrowable();
        List<Throwable> list = ExceptionUtils.getThrowableList(exception);
        for (Throwable throwable : list) {
            if (className.contains(throwable.getClass().getName()) && isDateOutOfRangeMessage(throwable.getMessage())) {
                doHandle(app, throwable.getClass().getName(), throwable.getMessage(), throwable);
                return true;
            }
            if (throwable instanceof RemoteException) {
                RemoteException remoteException = (RemoteException) throwable;
                for (RemoteException.Cause cause : remoteException.getCauses()) {
                    if (className.contains(cause.getClassName()) && isDateOutOfRangeMessage(throwable.getMessage())) {
                        doHandle(app, cause.getClassName(), cause.getMessage(), cause.getThrowable());
                        return true;
                    }
                }
            }
        }
        return false;
    }

    protected boolean isDateOutOfRangeMessage(String message) {
        return message != null && message.contains(MESSAGE);
    }

    protected void doHandle(App app, String className, String message, @Nullable Throwable throwable) {
        Messages messages = AppBeans.get(Messages.NAME);
        String msg = messages.formatMessage(getClass(), "mssqlDateOutOfRangeException.message");
        app.getWindowManager().showNotification(msg, IFrame.NotificationType.WARNING);
    }
}