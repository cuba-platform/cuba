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
import com.itmill.toolkit.terminal.Terminal;

import java.util.LinkedList;

public class ExceptionHandlers
{
    private LinkedList<ExceptionHandler> handlers = new LinkedList<ExceptionHandler>();

    private App app;

    private ExceptionHandler defaultHandler;

    public ExceptionHandlers(App app) {
        this.app = app;
        this.defaultHandler = new DefaultExceptionHandler();
    }

    public void addHandler(ExceptionHandler handler) {
        if (!handlers.contains(handler))
            handlers.add(handler);
    }

    public LinkedList<ExceptionHandler> getHandlers() {
        return handlers;
    }

    public void handle(Terminal.ErrorEvent event) {
        for (ExceptionHandler handler : handlers) {
            if (handler.handle(event, app))
                return;
        }
        defaultHandler.handle(event, app);
    }
}
