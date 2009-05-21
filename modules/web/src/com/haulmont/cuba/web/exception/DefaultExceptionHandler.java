/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 20.05.2009 18:32:01
 *
 * $Id$
 */
package com.haulmont.cuba.web.exception;

import com.itmill.toolkit.terminal.*;
import com.itmill.toolkit.terminal.gwt.server.ChangeVariablesErrorEvent;
import com.itmill.toolkit.ui.AbstractComponent;
import com.haulmont.cuba.web.App;

import java.net.SocketException;

public class DefaultExceptionHandler implements ExceptionHandler
{
    public boolean handle(Terminal.ErrorEvent event, App app) {
        // Copied from com.itmill.toolkit.Application.terminalError()

        Throwable t = event.getThrowable();
        if (t instanceof SocketException) {
            // Most likely client browser closed socket
//            System.err
//                    .println("Warning: SocketException in CommunicationManager."
//                            + " Most likely client (browser) closed socket.");
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
        } else {
            /*
             * Can't show it to the user in any way so we print to standard
             * error
             */
//            t.printStackTrace();
        }
        return true;
    }
}
