/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.web.exception;

import com.haulmont.cuba.web.App;
import com.vaadin.server.ErrorEvent;
import com.vaadin.server.VariableOwner;
//import com.vaadin.terminal.gwt.server.ChangeVariablesErrorEvent;
import com.vaadin.ui.AbstractComponent;
//import com.haulmont.cuba.web.AppUI;
import com.vaadin.ui.UI;
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
        // Copied from com.vaadin.Application.terminalError()

        Throwable t = event.getThrowable();
        if (t instanceof SocketException) {
            // Most likely client browser closed socket
            return true;
        }

        // Finds the original source of the error/exception
        /*Object owner = null;
        if (event instanceof VariableOwner.ErrorEvent) {
            owner = ((VariableOwner.ErrorEvent) event).getVariableOwner();
        } else if (event instanceof URIHandler.ErrorEvent) {
            owner = ((URIHandler.ErrorEvent) event).getURIHandler();
        } else if (event instanceof ParameterHandler.ErrorEvent) {
            owner = ((ParameterHandler.ErrorEvent) event).getParameterHandler();
        } else if (event instanceof ChangeVariablesErrorEvent) {
            owner = ((ChangeVariablesErrorEvent) event).getComponent();
        }

        // Shows the error in AbstractComponent
        if (owner instanceof AbstractComponent) {
            final Throwable e = event.getThrowable();
            if (e instanceof ErrorMessage) {
                ((AbstractComponent) owner).setComponentError((ErrorMessage) e);
            } else {
                ((AbstractComponent) owner)
                        .setComponentError(new SystemError(e));
            }
        }*/

        log.error("Unhandled error", event.getThrowable());

        return true;
    }
}
