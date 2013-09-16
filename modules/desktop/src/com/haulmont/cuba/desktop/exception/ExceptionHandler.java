/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.exception;

/**
 * Interface of all unhandled exception handlers in desktop UI<br/>
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public interface ExceptionHandler {

    /**
     * Handles exception if can
     *
     * @param thread
     * @param exception exception
     * @return true if the exception has been handled, false otherwise
     */
    boolean handle(Thread thread, Throwable exception);
}
