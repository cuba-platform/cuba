/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.exception;

import com.google.common.collect.Lists;
import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.gui.components.IFrame;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.remoting.RemoteInvocationFailureException;

import javax.annotation.Nullable;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;
import java.util.List;

/**
 * Entity serialization errors can happen during communication between server and client on either sides
 * when OpenJPA entities are being restored from byte stream,
 * in case client and server have different versions of entity classes.
 *
 * @author Alexander Budarov
 * @version $Id$
 */
public class EntitySerializationExceptionHandler extends AbstractExceptionHandler {

    public EntitySerializationExceptionHandler() {
        super(
                RemoteAccessException.class.getName(), // occurred on client
                RemoteInvocationFailureException.class.getName() // occurred on server
        );
    }

    @Override
    protected void doHandle(Thread thread, String className, String message, @Nullable Throwable throwable) {
        Messages messages = AppBeans.get(Messages.NAME);
        String title = messages.getMessage(getClass(), "entitySerializationException.title");
        String msg = messages.getMessage(getClass(), "entitySerializationException.description");
        App.getInstance().getMainFrame().showNotification(title, msg, IFrame.NotificationType.ERROR_HTML);
    }

    @Override
    protected boolean canHandle(String className, String message, @Nullable Throwable throwable) {
        Throwable cause = throwable != null ? throwable.getCause() : null;
        if (cause == null) {
            return false;
        }

        if (cause instanceof ClassCastException && checkClassCastExceptionStack(cause.getStackTrace())) {
            return true;
        }

        if (cause instanceof OptionalDataException && checkOptionalDataExceptionStack(cause.getStackTrace())) {
            return true;
        }
        return false;
    }

    private boolean checkClassCastExceptionStack(StackTraceElement[] stackTrace) {
        if (stackTrace.length < 1) {
            return false;
        }
        final List<String> knownMethods = Lists.newArrayList("pcReadUnmanaged", "readExternalFields", "readExternal");
        String methodName = stackTrace[0].getMethodName();
        return knownMethods.contains(methodName) && isEntityClass(stackTrace[0].getClassName());
    }

    private boolean checkOptionalDataExceptionStack(StackTraceElement[] stackTrace) {
        if (stackTrace.length < 1
                        || !ObjectInputStream.class.getName().equals(stackTrace[0].getClassName())) {
            return false;
        }

        for (StackTraceElement element: stackTrace) {
            if ("readExternal".equals(element.getMethodName())
                    && isEntityClass(element.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private boolean isEntityClass(String className) {
        Metadata metadata = AppBeans.get(Metadata.NAME);
        try {
            Class entityClass = ReflectionHelper.loadClass(className);
            MetaClass metaClass = metadata.getClass(entityClass);
            return metaClass != null;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
