/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.exception;

import com.haulmont.cuba.web.App;
import com.vaadin.server.AbstractErrorMessage;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.ErrorEvent;
import com.vaadin.server.ErrorMessage;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Window;
import org.apache.commons.lang.exception.ExceptionUtils;

import java.net.SocketException;

/**
 * This exception handler comes into play if no other handler in the chain has handled the exception.
 *
 * @author krivopustov
 * @version $Id$
 */
public class DefaultExceptionHandler implements ExceptionHandler {

    @Override
    public boolean handle(ErrorEvent event, App app) {
        // Copied from com.vaadin.server.DefaultErrorHandler.doDefault()

        //noinspection ThrowableResultOfMethodCallIgnored
        Throwable t = event.getThrowable();
        if (t instanceof SocketException) {
            // Most likely client browser closed socket
            return true;
        }

        if (t != null) {
            showDialog(t);
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

    protected void showDialog(Throwable exception) {
        Throwable rootCause = ExceptionUtils.getRootCause(exception);
        if (rootCause == null)
            rootCause = exception;
        ExceptionDialog dialog = new ExceptionDialog(rootCause);
        for (Window window : App.getInstance().getAppUI().getWindows()) {
            if (window.isModal()) {
                dialog.setModal(true);
                break;
            }
        }
        App.getInstance().getAppUI().addWindow(dialog);
        dialog.focus();
    }
}