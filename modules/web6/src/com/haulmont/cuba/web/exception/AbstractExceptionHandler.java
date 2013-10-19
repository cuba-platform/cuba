/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.exception;

import com.haulmont.cuba.core.global.RemoteException;
import com.haulmont.cuba.web.App;
import com.vaadin.terminal.Terminal;
import org.apache.commons.lang.exception.ExceptionUtils;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

/**
 * Base class for exception handlers determining their ability to handle an exception by its class name.
 *
 * <p>If you need to handle a specific exception, create a descendant of this class,
 * pass handling exception class names into constructor, implement
 * {@link #doHandle(com.haulmont.cuba.web.App, String, String, Throwable)} method
 * and register the new handler in the definition of {@link ExceptionHandlersConfiguration} bean in the client's
 * spring.xml.
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public abstract class AbstractExceptionHandler implements ExceptionHandler {

    private List<String> classNames;

    protected AbstractExceptionHandler(String... classNames) {
        this.classNames = Arrays.asList(classNames);
    }

    @Override
    public boolean handle(Terminal.ErrorEvent event, App app) {
        Throwable exception = event.getThrowable();
        List<Throwable> list = ExceptionUtils.getThrowableList(exception);
        for (Throwable throwable : list) {
            if (classNames.contains(throwable.getClass().getName())) {
                doHandle(app, throwable.getClass().getName(), throwable.getMessage(), throwable);
                return true;
            }
            if (throwable instanceof RemoteException) {
                RemoteException remoteException = (RemoteException) throwable;
                for (RemoteException.Cause cause : remoteException.getCauses()) {
                    if (classNames.contains(cause.getClassName())) {
                        doHandle(app, cause.getClassName(), cause.getMessage(), cause.getThrowable());
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Perform exception handling.
     * @param app       current {@link App} instance
     * @param className actual exception class name
     * @param message   exception message
     * @param throwable exception instance. Can be null if the exception occured on the server side and this
     * exception class isn't accessible by the client.
     */
    protected abstract void doHandle(App app, String className, String message, @Nullable Throwable throwable);
}
