/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.exception;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.gui.components.IFrame;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.remoting.RemoteAccessException;

import java.util.List;

/**
 * @author krivopustov
 * @version $Id$
 */
public class ConnectExceptionHandler implements ExceptionHandler {

    @Override
    public boolean handle(Thread thread, Throwable exception) {
        @SuppressWarnings("unchecked")
        List<Throwable> list = ExceptionUtils.getThrowableList(exception);
        for (Throwable throwable : list) {
            if (throwable instanceof RemoteAccessException) {
                Messages messages = AppBeans.get(Messages.NAME);
                String msg = messages.getMessage(getClass(), "connectException.message");
                if (throwable.getCause() == null) {
                    App.getInstance().getMainFrame().showNotification(msg, IFrame.NotificationType.ERROR);
                } else {
                    String description = messages.formatMessage(getClass(), "connectException.description",
                            throwable.getCause().toString());
                    App.getInstance().getMainFrame().showNotification(msg, description, IFrame.NotificationType.ERROR);
                }
                return true;
            }
        }
        return false;
    }
}
