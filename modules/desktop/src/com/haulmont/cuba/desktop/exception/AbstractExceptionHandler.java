/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.exception;

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.core.global.RemoteException;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.gui.AppConfig;
import org.apache.commons.lang.exception.ExceptionUtils;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

/**
 * Base class for exception handlers determining their ability to handle an exception by its class name.
 *
 * <p>If you need to handle a specific exception, create a descendant of this class,
 * pass handling exception class names into constructor, implement
 * {@link #doHandle(Thread, String, String, Throwable)} method
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
    @SuppressWarnings("unchecked")
    public boolean handle(Thread thread, Throwable exception) {
        List<Throwable> list = ExceptionUtils.getThrowableList(exception);
        for (Throwable throwable : list) {
            if (classNames.contains(throwable.getClass().getName())) {
                doHandle(thread, throwable.getClass().getName(), throwable.getMessage(), throwable);
                return true;
            }
            if (throwable instanceof RemoteException) {
                RemoteException remoteException = (RemoteException) throwable;
                for (RemoteException.Cause cause : remoteException.getCauses()) {
                    if (classNames.contains(cause.getClassName())) {
                        doHandle(thread, cause.getClassName(), cause.getMessage(), cause.getThrowable());
                        return true;
                    }
                }
            }
        }
        return false;
    }

    protected abstract void doHandle(Thread thread, String className, String message, @Nullable Throwable throwable);

    protected String getMessage(String key) {
        return MessageProvider.getMessage(AppConfig.getMessagesPack(), key, App.getInstance().getLocale());
    }
}
