/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.exception;

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.web.App;
import com.vaadin.data.Validator;
import com.vaadin.ui.Window;

import javax.annotation.Nullable;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class InvalidValueExceptionHandler extends AbstractExceptionHandler {

    public InvalidValueExceptionHandler() {
        super(Validator.InvalidValueException.class.getName());
    }

    @Override
    protected void doHandle(App app, String className, String message, @Nullable Throwable throwable) {
        app.getAppWindow().showNotification(
                MessageProvider.getMessage(getClass(), "validationFail.caption"),
                MessageProvider.getMessage(getClass(), "validationFail"),
                Window.Notification.TYPE_TRAY_NOTIFICATION
        );
    }
}
