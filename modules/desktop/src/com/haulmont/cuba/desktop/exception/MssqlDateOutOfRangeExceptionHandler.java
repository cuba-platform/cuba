/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.exception;

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.gui.components.IFrame;

import javax.annotation.Nullable;
import java.sql.SQLException;

/**
 * <p>$Id$</p>
 *
 * @author Novikov
 */
public class MssqlDateOutOfRangeExceptionHandler extends AbstractExceptionHandler {

    private static final String MESSAGE = "Only dates between January 1, 1753 and December 31, 9999 are accepted";

    public MssqlDateOutOfRangeExceptionHandler() {
        super(SQLException.class.getName());
    }

    @Override
    protected void doHandle(Thread thread, String className, String message, @Nullable Throwable throwable) {
        if (SQLException.class.getName().equals(className)) {
            boolean dateBetweenException = message != null ? message.contains(MESSAGE) : false;
            if (dateBetweenException) {
                String msg = MessageProvider.formatMessage(getClass(), "mssqlDateOutOfRangeException.message");
                App.getInstance().showNotification(msg, IFrame.NotificationType.ERROR);
            }
        }

    }
}
