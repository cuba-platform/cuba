/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.portal.sys.exception;

import com.haulmont.cuba.core.global.RemoteException;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * Base class for exception handlers determining their ability to handle an exception by its class name.
 *
 * <p>If you need to handle a specific exception, create a descendant of this class,
 * pass handling exception class names into constructor, implement
 * {@link #doHandle(HttpServletRequest, HttpServletResponse, String, String, Throwable)} method
 * and register the new handler in the definition of {@link ExceptionHandlersConfiguration} bean in the client's
 * spring.xml.
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public abstract class AbstractExceptionHandler implements ExceptionHandler {

    protected List<String> classNames;

    protected AbstractExceptionHandler(String... classNames) {
        this.classNames = Arrays.asList(classNames);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        List<Throwable> list = ExceptionUtils.getThrowableList(ex);
        for (Throwable throwable : list) {
            if (classNames.contains(throwable.getClass().getName())) {
                return doHandle(request, response, throwable.getClass().getName(), throwable.getMessage(), throwable);
            }
            if (throwable instanceof RemoteException) {
                RemoteException remoteException = (RemoteException) throwable;
                for (RemoteException.Cause cause : remoteException.getCauses()) {
                    if (classNames.contains(cause.getClassName())) {
                        return doHandle(request, response, cause.getClassName(), cause.getMessage(), cause.getThrowable());
                    }
                }
            }
        }
        return null;
    }

    protected abstract ModelAndView doHandle(HttpServletRequest request, HttpServletResponse response,
                                             String className, String message, @Nullable Throwable throwable);
}
