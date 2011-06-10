/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
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
