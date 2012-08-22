/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.exception;

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.gui.components.IFrame;
import org.springframework.remoting.RemoteAccessException;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class ConnectExceptionHandler implements ExceptionHandler {

    @Override
    public boolean handle(Thread thread, Throwable exception) {
        if (exception instanceof RemoteAccessException) {
            String msg = MessageProvider.getMessage(getClass(), "connectException.message");
            if (exception.getCause() == null) {
                App.getInstance().showNotification(msg, IFrame.NotificationType.ERROR);
            } else {
                String description = MessageProvider.formatMessage(getClass(), "connectException.description",
                        exception.getCause().toString());
                App.getInstance().showNotification(msg, description, IFrame.NotificationType.ERROR);
            }
            return true;
        }
        else
            return false;
    }
}
