/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.portal.sys.exception;

import com.haulmont.cuba.security.global.NoUserSessionException;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Handles {@link com.haulmont.cuba.security.global.NoUserSessionException}.
 *
 * @author artamonov
 * @version $Id$
 */
public class NoUserSessionHandler extends AbstractExceptionHandler {

    public NoUserSessionHandler() {
        super(NoUserSessionException.class.getName());
    }

    @Override
    protected ModelAndView doHandle(HttpServletRequest request, HttpServletResponse response, String className,
                                    String message, @Nullable Throwable throwable) {
        request.getSession().invalidate();
        return new ModelAndView("redirect:/");
    }
}
