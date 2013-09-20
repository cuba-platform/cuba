/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.exception;

import com.haulmont.cuba.core.global.RemoteException;
import org.apache.commons.lang.exception.ExceptionUtils;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

/**
 * Base class for exception handlers determining their ability to handle an exception by its class name and optional
 * implementation of {@link #canHandle(String, String, Throwable)} method.
 *
 * <p>If you need to handle a specific exception, create a descendant of this class,
 * pass handling exception class names into constructor, implement
 * {@link #doHandle(Thread, String, String, Throwable)} method
 * and register the new handler in the definition of {@link ExceptionHandlersConfiguration} bean in the client's
 * spring.xml.
 *
 * @author krivopustov
 * @version $Id$
 */
public abstract class AbstractExceptionHandler implements ExceptionHandler {

    private List<String> classNames;

    protected AbstractExceptionHandler(String... classNames) {
        this.classNames = Arrays.asList(classNames);
    }

    @Override
    public boolean handle(Thread thread, Throwable exception) {
        //noinspection unchecked
        List<Throwable> list = ExceptionUtils.getThrowableList(exception);
        for (Throwable throwable : list) {
            if (classNames.contains(throwable.getClass().getName())
                    && canHandle(throwable.getClass().getName(), throwable.getMessage(), throwable)) {
                doHandle(thread, throwable.getClass().getName(), throwable.getMessage(), throwable);
                return true;
            }
            if (throwable instanceof RemoteException) {
                RemoteException remoteException = (RemoteException) throwable;
                for (RemoteException.Cause cause : remoteException.getCauses()) {
                    if (classNames.contains(cause.getClassName())
                            && canHandle(throwable.getClass().getName(), throwable.getMessage(), throwable)) {
                        doHandle(thread, cause.getClassName(), cause.getMessage(), cause.getThrowable());
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Should be implemented in subclasses if the exception class name is not enough to determine the ability to
     * handle the exception.
     *
     * @param className exception class name
     * @param message   exception message
     * @param throwable exception instance. Can be null if the exception occured on the server side and this
     *                  exception class isn't accessible by the client.
     * @return true if the exception can be handled by this handler
     */
    protected boolean canHandle(String className, String message, @Nullable Throwable throwable) {
        return true;
    }

    /**
     * Perform exception handling.
     *
     * @param thread    current thread
     * @param className actual exception class name
     * @param message   exception message
     * @param throwable exception instance. Can be null if the exception occured on the server side and this
     * exception class isn't accessible by the client.
     */
    protected abstract void doHandle(Thread thread, String className, String message, @Nullable Throwable throwable);
}
