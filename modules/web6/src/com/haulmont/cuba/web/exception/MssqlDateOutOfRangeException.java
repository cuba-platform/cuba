/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.exception;

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.core.global.RemoteException;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.web.App;
import com.vaadin.terminal.Terminal;
import com.vaadin.ui.Window;
import org.apache.commons.lang.exception.ExceptionUtils;

import javax.annotation.Nullable;
import java.sql.SQLException;
import java.util.List;

/**
 * <p>$Id$</p>
 *
 * @author Novikov
 */
public class MssqlDateOutOfRangeException implements ExceptionHandler {

    private String className;

    private static final String MESSAGE = "Only dates between January 1, 1753 and December 31, 9999 are accepted";

    public MssqlDateOutOfRangeException() {
        this.className = SQLException.class.getName();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean handle(Terminal.ErrorEvent event, App app) {
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
        return message != null ? message.contains(MESSAGE) : false;
    }

    protected void doHandle(App app, String className, String message, @Nullable Throwable throwable) {
        String msg = MessageProvider.formatMessage(getClass(), "mssqlDateOutOfRangeException.message");
        app.getAppWindow().showNotification(msg, Window.Notification.TYPE_WARNING_MESSAGE);
    }
}
