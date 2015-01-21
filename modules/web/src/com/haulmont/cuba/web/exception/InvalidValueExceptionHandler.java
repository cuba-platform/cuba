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
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.ErrorEvent;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;

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
    public boolean handle(ErrorEvent event, App app) {
        boolean handled = super.handle(event, app);

        //noinspection ThrowableResultOfMethodCallIgnored
        if (handled && event.getThrowable() != null) {
            // Finds the original source of the error/exception
            AbstractComponent component = DefaultErrorHandler.findAbstractComponent(event);
            if (component != null) {
                component.markAsDirty();
            }

            if (component instanceof Component.Focusable) {
                ((Component.Focusable) component).focus();
            }
        }
        return handled;
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