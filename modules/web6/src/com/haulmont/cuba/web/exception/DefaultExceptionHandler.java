/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.exception;

import com.vaadin.terminal.*;
import com.vaadin.terminal.gwt.server.ChangeVariablesErrorEvent;
import com.vaadin.ui.AbstractComponent;
import com.haulmont.cuba.web.App;

import java.net.SocketException;

/**
 * This exception handler comes into play if no other handler in the chain has handled the exception.
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class DefaultExceptionHandler implements ExceptionHandler
{
    @Override
    public boolean handle(Terminal.ErrorEvent event, App app) {
        // Copied from com.vaadin.Application.terminalError()

        Throwable t = event.getThrowable();
        if (t instanceof SocketException) {
            // Most likely client browser closed socket
            return true;
        }

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

        // Shows the error in AbstractComponent
        if (owner instanceof AbstractComponent) {
            final Throwable e = event.getThrowable();
            if (e instanceof ErrorMessage) {
                ((AbstractComponent) owner).setComponentError((ErrorMessage) e);
            } else {
                ((AbstractComponent) owner)
                        .setComponentError(new SystemError(e));
            }
        }
        return true;
    }
}
