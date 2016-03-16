/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.haulmont.cuba.web.exception;

import com.haulmont.cuba.core.global.RemoteException;
import com.haulmont.cuba.web.App;
import com.vaadin.server.ErrorEvent;
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
 * {@link #doHandle(com.haulmont.cuba.web.App, String, String, Throwable)} method
 * and register the new handler in the definition of {@link ExceptionHandlersConfiguration} bean in the client's
 * spring.xml.
 *
 */
public abstract class AbstractExceptionHandler implements ExceptionHandler {

    private List<String> classNames;

    protected AbstractExceptionHandler(String... classNames) {
        this.classNames = Arrays.asList(classNames);
    }

    @Override
    public boolean handle(ErrorEvent event, App app) {
        Throwable exception = event.getThrowable();
        //noinspection unchecked
        List<Throwable> list = ExceptionUtils.getThrowableList(exception);
        for (Throwable throwable : list) {
            if (classNames.contains(throwable.getClass().getName())
                    && canHandle(throwable.getClass().getName(), throwable.getMessage(), throwable)) {
                doHandle(app, throwable.getClass().getName(), throwable.getMessage(), throwable);
                return true;
            }
            if (throwable instanceof RemoteException) {
                RemoteException remoteException = (RemoteException) throwable;
                for (RemoteException.Cause cause : remoteException.getCauses()) {
                    if (classNames.contains(cause.getClassName())
                            && canHandle(cause.getClassName(), cause.getMessage(), cause.getThrowable())) {
                        doHandle(app, cause.getClassName(), cause.getMessage(), cause.getThrowable());
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
     * @param app       current {@link App} instance
     * @param className actual exception class name
     * @param message   exception message
     * @param throwable exception instance. Can be null if the exception occured on the server side and this
     * exception class isn't accessible by the client.
     */
    protected abstract void doHandle(App app, String className, String message, @Nullable Throwable throwable);
}
