/*
 * Copyright (c) 2008-2019 Haulmont.
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
 */

package com.haulmont.cuba.web.testsupport.proxy;

import com.haulmont.cuba.web.sys.remoting.LocalServiceProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * Provides default stubs for middleware services. Can also be used to register service mocks specific to a test,
 * see {@link #mock(Class, Object)} and {@link #clear()} methods.
 */
public class TestServiceProxy extends LocalServiceProxy {

    private static final Logger log = LoggerFactory.getLogger(TestServiceProxy.class);

    private static Map<Class, Object> defaults = new HashMap<>();
    private static Map<Class, Object> mocks = new HashMap<>();

    @Override
    public Object getObject() {
        return Proxy.newProxyInstance(
                getBeanClassLoader(),
                new Class[]{getServiceInterface()},
                new ServiceInvocationHandler(getServiceInterface()));
    }

    /**
     * Registers a default implementation for the given service interface which will be used if no mocks are specified.
     *
     * @param serviceInterface service interface, e.g. {@code DataService}
     * @param defaultProxy service implementation. If null, default implementation does nothing and returns null.
     *
     * @see #mock(Class, Object)
     */
    public static <T> void setDefault(Class<T> serviceInterface, @Nullable T defaultProxy) {
        defaults.put(serviceInterface, defaultProxy);
    }

    /**
     * Returns a default implementation of the given service interface.
     *
     * @param serviceInterface service interface, e.g. {@code DataService}
     * @return the default implementation or null. In the latter case, the implementation does nothing and returns null.
     */
    @SuppressWarnings("unchecked")
    @Nullable
    public static <T> T getDefault(Class<T> serviceInterface) {
        return (T) defaults.get(serviceInterface);
    }

    /**
     * Registers a mock implementation for the given service interface.
     *
     * @param serviceInterface service interface, e.g. {@code DataService}
     * @param mock mock implementation. If null, the default implementation will be used.
     *
     * @see #clear()
     */
    public static <T> void mock(Class<T> serviceInterface, @Nullable T mock) {
        mocks.put(serviceInterface, mock);
    }

    /**
     * Clears all previously registered mocks.
     *
     * @see #mock(Class, Object)
     */
    public static void clear() {
        mocks.clear();
    }

    protected class ServiceInvocationHandler implements InvocationHandler {

        protected Class serviceInterface;

        public ServiceInvocationHandler(Class serviceInterface) {
            this.serviceInterface = serviceInterface;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Object mock = mocks.get(serviceInterface);
            if (mock != null) {
                log.info("Invoking {} on {}", method, mock);
                return method.invoke(mock, args);
            }

            Object defaultProxy = defaults.get(serviceInterface);
            if (defaultProxy != null) {
                log.info("Invoking {} on {}", method, defaultProxy);
                return method.invoke(defaultProxy, args);
            }

            log.info("Returning null from {}", method);
            return null;
        }
    }
}