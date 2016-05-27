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

package com.haulmont.cuba.core.sys.remoting;

import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.core.sys.serialization.SerializationSupport;
import org.apache.commons.lang.ClassUtils;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class LocalServiceInvokerImpl implements LocalServiceInvoker {

    private Object target;

    public LocalServiceInvokerImpl(Object target) {
        if (target == null)
            throw new IllegalArgumentException("Target object is null");

        this.target = target;
    }

    @Override
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
                Class<?> paramClass = ClassUtils.getClass(classLoader, parameterTypeNames[i]);
                parameterTypes[i] = paramClass;
            }

            byte[][] argumentsData = invocation.getArgumentsData();
            Object[] notSerializableArguments = invocation.getNotSerializableArguments();
            Object[] arguments;
            if (argumentsData == null)
                arguments = null;
            else {
                arguments = new Object[argumentsData.length];
                for (int i = 0; i < argumentsData.length; i++) {
                    if (argumentsData[i] == null) {
                        if (notSerializableArguments[i] == null) {
                            arguments[i] = null;
                        } else {
                            arguments[i] = notSerializableArguments[i];
                        }
                    } else {
                        arguments[i] = SerializationSupport.deserialize(argumentsData[i]);
                    }
                }
            }

            if (invocation.getSessionId() != null)
                AppContext.setSecurityContext(new SecurityContext(invocation.getSessionId()));
            else
                AppContext.setSecurityContext(null);

            Method method = target.getClass().getMethod(invocation.getMethodName(), parameterTypes);
            Object data = method.invoke(target, arguments);

            if (data instanceof Serializable) {
                result.setData(SerializationSupport.serialize(data));
            } else {
                result.setNotSerializableData(data);
            }
            return result;
        } catch (Throwable t) {
            if (t instanceof InvocationTargetException)
                t = ((InvocationTargetException) t).getTargetException();
            result.setException(SerializationSupport.serialize(t));
            return result;
        } finally {
            Thread.currentThread().setContextClassLoader(clientClassLoader);
            AppContext.setSecurityContext(null);
        }
    }
}