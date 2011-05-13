/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.sys.remoting;

import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.remoting.LocalServiceDirectory;
import com.haulmont.cuba.core.sys.remoting.LocalServiceInvocation;
import com.haulmont.cuba.core.sys.remoting.LocalServiceInvocationResult;
import com.haulmont.cuba.core.sys.remoting.LocalServiceInvoker;
import org.apache.commons.lang.SerializationUtils;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.remoting.support.RemoteAccessor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class LocalServiceProxy extends RemoteAccessor implements FactoryBean<Object>, InitializingBean {

    private Object serviceProxy;
    private String serviceName;

    public void afterPropertiesSet() {
        if (serviceName == null)
            throw new IllegalStateException("Property 'serviceName' is required");
        if (getServiceInterface() == null)
            throw new IllegalStateException("Property 'serviceInterface' is required");

        serviceProxy = Proxy.newProxyInstance(
                getBeanClassLoader(),
                new Class[] {getServiceInterface()},
                new LocalServiceInvocationHandler(serviceName)
        );
    }

    public Object getObject() throws Exception {
        return serviceProxy;
    }

    public Class<?> getObjectType() {
        return getServiceInterface();
    }

    public boolean isSingleton() {
        return true;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        if (serviceName.startsWith("/"))
            this.serviceName = serviceName;
        else
            this.serviceName = "/" + serviceName;
    }

    private class LocalServiceInvocationHandler implements InvocationHandler {

        private String serviceName;

        public LocalServiceInvocationHandler(String serviceName) {
            this.serviceName = serviceName;
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            LocalServiceInvoker invoker = LocalServiceDirectory.getInvoker(serviceName);
            if (invoker == null)
                throw new IllegalArgumentException("Service " + serviceName + " is not registered in LocalServiceDirectory");

            Class<?>[] parameterTypes = method.getParameterTypes();
            String[] parameterTypeNames = new String[parameterTypes.length];
            for (int i = 0; i < parameterTypes.length; i++) {
                parameterTypeNames[i] = parameterTypes[i].getName();
            }

            byte[][] argumentsData;
            if (args == null)
                argumentsData = null;
            else {
                argumentsData = new byte[args.length][];
                for (int i = 0; i < args.length; i++) {
                    Serializable arg = (Serializable) args[i];
                    argumentsData[i] = SerializationUtils.serialize(arg);
                }
            }

            UUID sessionId = AppContext.getSecurityContext() == null ? null : AppContext.getSecurityContext().getSessionId();
            LocalServiceInvocation invocation = new LocalServiceInvocation(
                    method.getName(), parameterTypeNames, argumentsData, sessionId);

            LocalServiceInvocationResult result = invoker.invoke(invocation);

            if (result.getException() != null) {
                Throwable t = (Throwable) deserialize(result.getException());
                throw t;
            } else {
                Object data = deserialize(result.getData());
                return data;
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
}
