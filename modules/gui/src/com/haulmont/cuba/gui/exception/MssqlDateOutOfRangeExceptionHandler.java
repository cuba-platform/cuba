/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.exception;

import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.RemoteException;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.Frame;
import org.apache.commons.lang.exception.ExceptionUtils;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.sql.SQLException;
import java.util.List;

/**
 * @author novikov
 * @version $Id$
 */
@ManagedBean("cuba_MssqlDateOutOfRangeExceptionHandler")
public class MssqlDateOutOfRangeExceptionHandler implements GenericExceptionHandler {

    protected String className;

    protected static final String MESSAGE = "Only dates between January 1, 1753 and December 31, 9999 are accepted";

    @Inject
    protected Messages messages;

    public MssqlDateOutOfRangeExceptionHandler() {
        this.className = SQLException.class.getName();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean handle(Throwable exception, WindowManager windowManager) {
        List<Throwable> list = ExceptionUtils.getThrowableList(exception);
        for (Throwable throwable : list) {
            if (className.contains(throwable.getClass().getName()) && isDateOutOfRangeMessage(throwable.getMessage())) {
                doHandle(windowManager);
                return true;
            }
            if (throwable instanceof RemoteException) {
                RemoteException remoteException = (RemoteException) throwable;
                for (RemoteException.Cause cause : remoteException.getCauses()) {
                    if (className.contains(cause.getClassName()) && isDateOutOfRangeMessage(throwable.getMessage())) {
                        doHandle(windowManager);
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

    protected void doHandle(WindowManager windowManager) {
        String msg = messages.formatMessage(getClass(), "mssqlDateOutOfRangeException.message");
        windowManager.showNotification(msg, Frame.NotificationType.WARNING);
    }
}