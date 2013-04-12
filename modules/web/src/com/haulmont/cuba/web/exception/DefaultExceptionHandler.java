/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.web.exception;

import com.haulmont.cuba.web.App;
import com.vaadin.server.AbstractErrorMessage;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.ErrorEvent;
import com.vaadin.server.ErrorMessage;
import com.vaadin.ui.AbstractComponent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.SocketException;

/**
 * This exception handler comes into play if no other handler in the chain has handled the exception.
 *
 * @author krivopustov
 * @version $Id$
 */
public class DefaultExceptionHandler implements ExceptionHandler {

    private final static Log log = LogFactory.getLog(DefaultExceptionHandler.class);

    @Override
    public boolean handle(ErrorEvent event, App app) {
        // Copied com.vaadin.server.DefaultErrorHandler.doDefault()

        //noinspection ThrowableResultOfMethodCallIgnored
        Throwable t = event.getThrowable();
        if (t instanceof SocketException) {
            // Most likely client browser closed socket
            return true;
        }

        // Finds the original source of the error/exception
        AbstractComponent component = DefaultErrorHandler.findAbstractComponent(event);
        if (component != null) {
            // Shows the error in AbstractComponent
            ErrorMessage errorMessage = AbstractErrorMessage
                    .getErrorMessageForException(t);
            component.setComponentError(errorMessage);
        }

        log.error("Unhandled error", event.getThrowable());

        return true;
    }
}