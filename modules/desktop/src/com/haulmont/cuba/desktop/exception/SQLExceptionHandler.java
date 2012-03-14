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
public class SQLExceptionHandler extends AbstractExceptionHandler {
    public SQLExceptionHandler() {
        super(SQLException.class.getName());
    }

    @Override
    protected void doHandle(Thread thread, String className, String message, @Nullable Throwable throwable) {
        if (SQLException.class.getName().equals(className)) {
            String tMessage = MessageProvider.getMessage(getClass(), "sqlException.text");
            boolean dateBetweenException = message != null ? message.contains(tMessage) : false;
            if (dateBetweenException) {
                String msg = MessageProvider.formatMessage(getClass(), "sqlException.message");
                App.getInstance().showNotification(msg, IFrame.NotificationType.ERROR);
            }
        }

    }
}
