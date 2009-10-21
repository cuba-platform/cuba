/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 28.07.2009 10:09:57
 *
 * $Id$
 */
package com.haulmont.cuba.web.exception;

import com.vaadin.terminal.Terminal;
import com.haulmont.cuba.web.App;

/**
 * Base class for exception handler bound to specific exception type.
 * <p>
 * If you need to handle a specific exception, create a descendant of this class,
 * pass handling exception class into constructor, implement {@link #doHandle(Throwable,com.haulmont.cuba.web.App)} method
 * and register the new handler in {@link App#initExceptionHandlers(boolean)}.
 */
public abstract class AbstractExceptionHandler<T extends Throwable> implements ExceptionHandler {

    private final Class<T> tClass;

    public AbstractExceptionHandler(Class<T> tClass) {
        this.tClass = tClass;
    }

    public boolean handle(Terminal.ErrorEvent event, App app) {
        Throwable t = event.getThrowable();
        while (t != null) {
            if (tClass.isAssignableFrom(t.getClass())) {
                doHandle((T) t, app);
                return true;
            }
            t = t.getCause();
        }
        return false;
    }

    protected abstract void doHandle(T t, App app);
}
