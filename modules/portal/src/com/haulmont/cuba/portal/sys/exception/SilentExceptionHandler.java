/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.portal.sys.exception;

import com.haulmont.cuba.core.global.SilentException;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Handler that does nothing in respond to {@link com.haulmont.cuba.core.global.SilentException}.
 *
 * @author artamonov
 * @version $Id$
 */
public class SilentExceptionHandler extends AbstractExceptionHandler {

    public SilentExceptionHandler() {
        super(SilentException.class.getName());
    }

    @Override
    protected ModelAndView doHandle(HttpServletRequest request, HttpServletResponse response,
                                    String className, String message, @Nullable Throwable throwable) {
        return new ModelAndView("");
    }
}
