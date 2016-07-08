/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.haulmont.cuba.web.exception;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.WebWindowManager;
import com.vaadin.server.AbstractErrorMessage;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.ErrorEvent;
import com.vaadin.server.ErrorMessage;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Window;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

import java.net.SocketException;

/**
 * This exception handler comes into play if no other handler in the chain has handled the exception.
 */
public class DefaultExceptionHandler implements ExceptionHandler {

    protected Messages messages = AppBeans.get(Messages.NAME);

    @Override
    public boolean handle(ErrorEvent event, App app) {
        // Copied from com.vaadin.server.DefaultErrorHandler.doDefault()

        //noinspection ThrowableResultOfMethodCallIgnored
        Throwable t = event.getThrowable();
        //noinspection ThrowableResultOfMethodCallIgnored
        if (t instanceof SocketException
                || ExceptionUtils.getRootCause(t) instanceof SocketException) {
            // Most likely client browser closed socket
            return true;
        }

        // Support Tomcat 8 ClientAbortException
        if (StringUtils.contains(ExceptionUtils.getMessage(t), "ClientAbortException")) {
            // Most likely client browser closed socket
            return true;
        }

        if (t != null) {
            if (app.getConnection().getSession() != null) {
                showDialog(app, t);
            } else {
                showNotification(app, t);
            }
        } else {
            // Finds the original source of the error/exception
            AbstractComponent component = DefaultErrorHandler.findAbstractComponent(event);
            if (component != null) {
                // Shows the error in AbstractComponent
                ErrorMessage errorMessage = AbstractErrorMessage.getErrorMessageForException(t);
                component.setComponentError(errorMessage);
            }
        }

        return true;
    }

    protected void showDialog(App app, Throwable exception) {
        Throwable rootCause = ExceptionUtils.getRootCause(exception);
        if (rootCause == null) {
            rootCause = exception;
        }
        ExceptionDialog dialog = new ExceptionDialog(rootCause);
        AppUI ui = app.getAppUI();
        for (Window window : ui.getWindows()) {
            if (window.isModal()) {
                dialog.setModal(true);
                break;
            }
        }
        ui.addWindow(dialog);
        dialog.focus();
    }

    protected void showNotification(App app, Throwable exception) {
        Throwable rootCause = ExceptionUtils.getRootCause(exception);
        if (rootCause == null) {
            rootCause = exception;
        }
        WebWindowManager windowManager = app.getWindowManager();
        if (windowManager != null) {
            windowManager.showNotification(
                    messages.getMainMessage("exceptionDialog.caption", app.getLocale()),
                    rootCause.getClass().getSimpleName() + (rootCause.getMessage() != null ? "\n" + rootCause.getMessage() : ""),
                    Frame.NotificationType.ERROR
            );
        }
    }
}