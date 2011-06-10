/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.exception;

import java.util.LinkedList;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class ExceptionHandlers {

    private LinkedList<ExceptionHandler> handlers = new LinkedList<ExceptionHandler>();

    private ExceptionHandler defaultHandler;

    public ExceptionHandlers() {
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
    public void handle(Thread thread, Throwable exception) {
        for (ExceptionHandler handler : handlers) {
            if (handler.handle(thread, exception))
                return;
        }
        defaultHandler.handle(thread, exception);
    }
}
