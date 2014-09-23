/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.exception;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.web.App;
import com.vaadin.data.Validator;

import javax.annotation.Nullable;

/**
 * @author krivopustov
 * @version $Id$
 */
public class InvalidValueExceptionHandler extends AbstractExceptionHandler {

    public InvalidValueExceptionHandler() {
        super(Validator.InvalidValueException.class.getName());
    }

    @Override
    protected void doHandle(App app, String className, String message, @Nullable Throwable throwable) {
        Messages messages = AppBeans.get(Messages.NAME);
        app.getWindowManager().showNotification(
                messages.getMessage(getClass(), "validationFail.caption"),
                messages.getMessage(getClass(), "validationFail"),
                IFrame.NotificationType.TRAY
        );
    }
}