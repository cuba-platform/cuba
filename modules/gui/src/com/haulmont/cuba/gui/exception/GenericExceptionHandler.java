/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.exception;

import com.haulmont.cuba.gui.WindowManager;

/**
 * Interface to be implemented by exception handlers defined on GUI level.
 *
 * @author krivopustov
 * @version $Id$
 */
public interface GenericExceptionHandler {

    /**
     * Defines the highest precedence for {@link org.springframework.core.Ordered} platform handlers.
     */
    int HIGHEST_PLATFORM_PRECEDENCE = 100;

    /**
     * Defines the lowest precedence for {@link org.springframework.core.Ordered} platform handlers.
     */
    int LOWEST_PLATFORM_PRECEDENCE = 1000;

    /**
     * Handle an exception. Implementation class should either handle the exception and return true, or return false
     * to delegate execution to the next handler in the chain of responsibility.
     * @param exception     exception instance
     * @param windowManager WindowManager instance
     * @return              true if the exception has been succesfully handled, false if not
     */
    boolean handle(Throwable exception, WindowManager windowManager);
}
