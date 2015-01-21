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
import com.vaadin.terminal.ParameterHandler;
import com.vaadin.terminal.Terminal;
import com.vaadin.terminal.URIHandler;
import com.vaadin.terminal.VariableOwner;
import com.vaadin.terminal.gwt.server.ChangeVariablesErrorEvent;
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
    public boolean handle(Terminal.ErrorEvent event, App app) {
        boolean handled = super.handle(event, app);

        //noinspection ThrowableResultOfMethodCallIgnored
        if (handled && event.getThrowable() != null) {
            // Finds the original source of the error/exception
            Object owner = null;
            if (event instanceof VariableOwner.ErrorEvent) {
                owner = ((VariableOwner.ErrorEvent) event).getVariableOwner();
            } else if (event instanceof URIHandler.ErrorEvent) {
                owner = ((URIHandler.ErrorEvent) event).getURIHandler();
            } else if (event instanceof ParameterHandler.ErrorEvent) {
                owner = ((ParameterHandler.ErrorEvent) event).getParameterHandler();
            } else if (event instanceof ChangeVariablesErrorEvent) {
                owner = ((ChangeVariablesErrorEvent) event).getComponent();
            }

            if (owner instanceof Component.Focusable) {
                ((Component.Focusable) owner).focus();
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