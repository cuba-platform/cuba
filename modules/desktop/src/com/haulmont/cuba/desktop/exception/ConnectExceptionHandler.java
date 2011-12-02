/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.exception;

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.gui.components.IFrame;

import javax.annotation.Nullable;
import java.net.ConnectException;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class ConnectExceptionHandler extends AbstractExceptionHandler{

    public ConnectExceptionHandler() {
        super(ConnectException.class.getName());
    }

    @Override
    protected void doHandle(Thread thread, String className, String message, @Nullable Throwable throwable) {
        String msg = MessageProvider.getMessage(getClass(), "connectException.message");
        App.getInstance().showNotificationPopup(msg, IFrame.NotificationType.ERROR);
    }
}
