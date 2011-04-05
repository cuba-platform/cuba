/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.sys.remoting;

import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.core.sys.ServerSecurityUtils;
import org.apache.commons.lang.SerializationUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class LocalServiceInvokerImpl implements LocalServiceInvoker {

    private Object target;

    public LocalServiceInvokerImpl(Object target) {
        if (target == null)
            throw new IllegalArgumentException("Target object is null");

        this.target = target;
    }

    public LocalServiceInvocationResult invoke(LocalServiceInvocation invocation) {
        if (invocation == null)
            throw new IllegalArgumentException("Invocation is null");

        LocalServiceInvocationResult result = new LocalServiceInvocationResult();
        ClassLoader clientClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            ClassLoader classLoader = target.getClass().getClassLoader();
            Thread.currentThread().setContextClassLoader(classLoader);

            String[] parameterTypeNames = invocation.getParameterTypeNames();
            Class[] parameterTypes = new Class[parameterTypeNames.length];
            for (int i = 0; i < parameterTypeNames.length; i++) {
                Class<?> paramClass = classLoader.loadClass(parameterTypeNames[i]);
                parameterTypes[i] = paramClass;
            }

            byte[][] argumentsData = invocation.getArgumentsData();
            Object[] arguments;
            if (argumentsData == null)
                arguments = null;
            else {
                arguments = new Object[argumentsData.length];
                for (int i = 0; i < argumentsData.length; i++) {
                    if (argumentsData[i] == null)
                        arguments[i] = null;
                    else
                        arguments[i] = deserialize(argumentsData[i]);
                }
            }

            AppContext.setSecurityContext(new SecurityContext(invocation.getSessionId()));

            Method method = target.getClass().getMethod(invocation.getMethodName(), parameterTypes);
            Serializable data = (Serializable) method.invoke(target, arguments);

            result.setData(SerializationUtils.serialize(data));
            return result;
        } catch (Throwable t) {
            if (t instanceof InvocationTargetException)
                t = ((InvocationTargetException) t).getTargetException();
            result.setException(SerializationUtils.serialize(t));
            return result;
        } finally {
            Thread.currentThread().setContextClassLoader(clientClassLoader);
        }
    }

    // don't use SerializationUtils.deserialize() here to avoid ClassNotFoundException
    public Object deserialize(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        try {
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
            return ois.readObject();
        }
        catch (IOException ex) {
            throw new IllegalArgumentException("Failed to deserialize object", ex);
        }
        catch (ClassNotFoundException ex) {
            throw new IllegalStateException("Failed to deserialize object type", ex);
        }
    }
}
