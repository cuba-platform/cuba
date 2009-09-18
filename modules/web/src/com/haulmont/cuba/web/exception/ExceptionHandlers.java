/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 20.05.2009 18:19:11
 *
 * $Id$
 */
package com.haulmont.cuba.web.exception;

import com.haulmont.cuba.web.App;
import com.vaadin.terminal.Terminal;

import java.util.LinkedList;

/**
 * Provides extensible exception handling functionality.
 * See also {@link App#initExceptionHandlers(boolean)} 
 */
public class ExceptionHandlers
{
    private LinkedList<ExceptionHandler> handlers = new LinkedList<ExceptionHandler>();

    private App app;

    private ExceptionHandler defaultHandler;

    public ExceptionHandlers(App app) {
        this.app = app;
        this.defaultHandler = new DefaultExceptionHandler();
    }

    /**
     * Adds new handler if it is not yet registered
     */
    public void addHandler(ExceptionHandler handler) {
        if (!handlers.contains(handler))
            handlers.add(handler);
    }

    /**
     * All registered handlers
     */
    public LinkedList<ExceptionHandler> getHandlers() {
        return handlers;
    }

    /**
     * Delegates exception handling to registered handlers
     */
    public void handle(Terminal.ErrorEvent event) {
        for (ExceptionHandler handler : handlers) {
            if (handler.handle(event, app))
                return;
        }
        defaultHandler.handle(event, app);
    }
}
