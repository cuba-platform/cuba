/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.portal.sys.exception;

import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Interface of all unhandled exception handlers in desktop UI<br/>
 *
 * @author krivopustov
 * @version $Id$
 */
public interface ExceptionHandler {

    /**
     * Handles exception if can
     *
     * @param thread Thread
     * @param exception Exception
     * @return true If the exception has been handled, false otherwise
     */
    ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Exception ex);
}
